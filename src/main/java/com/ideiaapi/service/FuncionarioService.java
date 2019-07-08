package com.ideiaapi.service;

import com.ideiaapi.dto.FuncionarioDTO;
import com.ideiaapi.dto.RowsImportDTO;
import com.ideiaapi.dto.s3.AnexoS3DTO;
import com.ideiaapi.model.Empresa;
import com.ideiaapi.model.FuncCargoEmpresa;
import com.ideiaapi.model.Funcionario;
import com.ideiaapi.model.Usuario;
import com.ideiaapi.repository.FuncionarioRepository;
import com.ideiaapi.repository.filter.FuncionarioFilter;
import com.ideiaapi.repository.projection.ResumoFuncionario;
import com.ideiaapi.security.UsuarioSessao;
import com.ideiaapi.storage.S3;
import com.ideiaapi.util.datas.UtilsData;
import com.ideiaapi.validate.FuncionarioValidate;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FuncionarioService {

    private static final Logger log = LoggerFactory.getLogger(FuncionarioService.class);

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private FuncionarioValidate funcionarioValidate;

    @Autowired
    private FuncCargoEmpresaService funcCargoEmpresaService;

    @Autowired
    private EmpresaService empresaService;

    @Autowired
    private S3 s3;

    public AnexoS3DTO salvarFotoFuncionarioS3(MultipartFile file) {
        String nome = s3.salvarArquivoS3Temporatimente(file, Boolean.TRUE);
        return new AnexoS3DTO(nome, s3.configuraUrl(nome));
    }

    public Page<Funcionario> filtrar(FuncionarioFilter filter, Pageable pageable) {
        Page<Funcionario> filtrar = this.funcionarioRepository.filtrar(filter, pageable);
        Usuario usuarioLogado = UsuarioSessao.getUserLogado();
        if (!UsuarioSessao.isAdmin(usuarioLogado) && (null != filtrar.getContent() && !filtrar.getContent().isEmpty())) {

            filtrar.getContent().forEach(funcionario -> this.deparaCargoFuncionario(funcionario, usuarioLogado));

        }

        return filtrar;
    }

    public Page<ResumoFuncionario> resumo(FuncionarioFilter filter, Pageable pageable) {
        return this.funcionarioRepository.resumir(filter, pageable);
    }

    @Transactional
    public Funcionario cadastraFuncionario(Funcionario entity) {

        this.funcionarioValidate.fluxoCriacao(entity);
        if (StringUtils.hasText(entity.getAnexo())) {
            this.s3.salvar(entity.getAnexo());
        }
        this.calculaIdade(entity);
        this.geraNumeroCadastro(entity);
        Funcionario salvo = this.funcionarioRepository.save(entity);
        this.funcCargoEmpresaService.insereCargo(salvo);
        return salvo;
    }

    @Transactional
    public void insereEmBatch(List<Funcionario> funcionarios) {
        this.funcionarioRepository.save(funcionarios);
    }

    private void geraNumeroCadastro(Funcionario entity) {
        if (null == entity.getNumeroCadastro()) {
            Long next = this.funcionarioRepository.getProximoNumeroCadastroDisponivel();
            if (null == next) {
                next = 1L;
            }
            entity.setNumeroCadastro(next);
        }
    }

    public Funcionario buscaFuncionario(Long codigo) {
        return this.loadFuncionario(codigo);
    }

    private Funcionario loadFuncionario(Long codigo) {
        Funcionario funcionario = this.funcionarioRepository.findOne(codigo);

        if (funcionario == null) {
            throw new EmptyResultDataAccessException(1);
        }

        Usuario userLogado = UsuarioSessao.getUserLogado();
        if (!UsuarioSessao.isAdmin(userLogado)) {
            deparaCargoFuncionario(funcionario, userLogado);
        }
        return funcionario;
    }

    private void deparaCargoFuncionario(Funcionario funcionario, Usuario userLogado) {
        final FuncCargoEmpresa funcCargoEmpresa = this.funcCargoEmpresaService.getByCodFuncionarioAndCodEmpresa(funcionario,
                userLogado.getEmpresa().getCodigo());

        if (null != funcCargoEmpresa)
            funcionario.setCargo(funcCargoEmpresa.getCargo());
    }

    public void deletaFuncionario(Long codigo) {
        this.funcionarioRepository.delete(codigo);
    }

    public ResponseEntity<Funcionario> atualizaFuncionario(Long codigo, Funcionario funcionario) {
        Funcionario funcionarioSalvo = this.buscaFuncionario(codigo);

        this.equalizaEmpresas(funcionario, funcionarioSalvo);

        if (StringUtils.isEmpty(funcionario.getAnexo()) && StringUtils.hasText(funcionarioSalvo.getAnexo())) {
            this.s3.remover(funcionarioSalvo.getAnexo());
        } else if (StringUtils.hasText(funcionario.getAnexo()) && !funcionario.getAnexo().equals(funcionarioSalvo.getAnexo())) {
            this.s3.substituir(funcionarioSalvo.getAnexo(), funcionario.getAnexo());
        }
        BeanUtils.copyProperties(funcionario, funcionarioSalvo, "codigo");

        this.funcionarioValidate.fluxoAtualizacao(funcionarioSalvo);
        this.removeEmpresasDuplicadas(funcionarioSalvo);
        this.calculaIdade(funcionarioSalvo);
        this.geraNumeroCadastro(funcionarioSalvo);
        this.funcionarioRepository.save(funcionarioSalvo);
        this.funcCargoEmpresaService.insereCargo(funcionarioSalvo);
        return ResponseEntity.ok(funcionarioSalvo);
    }

    private void equalizaEmpresas(Funcionario funcionario, Funcionario funcionarioSalvo) {
        List<Empresa> empresas = funcionario.getEmpresas();

        if (null != empresas && !empresas.isEmpty() && (null != funcionarioSalvo.getEmpresas() && !funcionarioSalvo.getEmpresas().isEmpty())) {

            funcionarioSalvo.getEmpresas().forEach(empresas::add);
        }

    }

    private void removeEmpresasDuplicadas(Funcionario funcionarioSalvo) {
        Map<Long, Empresa> mapEmpresas = new HashMap<>();
        if (null != funcionarioSalvo.getEmpresas() && !funcionarioSalvo.getEmpresas().isEmpty()) {

            funcionarioSalvo.getEmpresas().forEach(emp -> mapEmpresas.put(emp.getCodigo(), emp));
            funcionarioSalvo.setEmpresas(new ArrayList<>());
            funcionarioSalvo.setEmpresas(mapEmpresas.values().stream().collect(Collectors.toList()));

        }
    }

    private void calculaIdade(Funcionario funcionario) {
        final int yearNow = LocalDate.now().getYear();
        final int nascimento = funcionario.getDataNascimento().getYear();
        funcionario.setIdade(yearNow - nascimento);
    }

    public Funcionario buscaFuncionarioPorCpf(String cpf) {
        return this.funcionarioRepository.findByCpf(cpf);
    }

    public List<Funcionario> listarTodos() {
        return this.funcionarioRepository.findAll();
    }

    public List<Funcionario> todos() {
        Usuario userLogado = UsuarioSessao.getUserLogado();
        if (!UsuarioSessao.isAdmin(userLogado)) {

            return this.funcionarioRepository.findAllByEmpresas(userLogado.getEmpresa().getCodigo());

        }
        return this.funcionarioRepository.findAll();
    }


    public byte[] folhaDeRegistro(Long codigo) throws Exception { //NOSONAR

        Funcionario funcionario = this.buscaFuncionario(codigo);

        Map<String, Object> parametros = new HashMap<>();

        parametros.put("FUNC_NOME", funcionario.getNome());

        String sexo = "( 1 )F ( 2 )M";
        String sx = funcionario.getSexo();
        if (null != sx && !"".equals(sx)) {
            if (sx.equals("FEMININO")) {
                sexo = sexo.replace("1", "X").replace("2", " ");
            } else if (sx.equals("MASCULINO")) {
                sexo = sexo.replace("1", " ").replace("2", "X");
            } else {
                sexo = sexo.replace("1", " ").replace("2", " ");
            }
        } else if (null == sx) {
            sexo = sexo.replace("1", " ").replace("2", " ");
        }

        parametros.put("FUNC_SEXO", sexo);
        parametros.put("FUNC_ESTADO_CIVIL", null != funcionario.getEstadoCivil() ? funcionario.getEstadoCivil() : "");
        parametros.put("FUNC_ESCOLARIDADE", null != funcionario.getEscolaridade() ? funcionario.getEscolaridade() : "");
        parametros.put("FUNC_CPF", null != funcionario.getCpf() ? funcionario.getCpf() : "");
        parametros.put("FUNC_PROFISSAO", null != funcionario.getCargo() ? funcionario.getCargo() : "");
        parametros.put("NUM_CADASTRO", String.valueOf(funcionario.getNumeroCadastro()));
        parametros.put("FUNC_NATURALIDADE", null != funcionario.getNaturalidade() ? funcionario.getNaturalidade() : "");
        parametros.put("FUNC_NASCIMENTO",
                null != funcionario.getDataNascimento() ? UtilsData.getDataConvertida(funcionario.getDataNascimento(), "dd/MM/yyyy") : "");
        parametros.put("EMP_NOME", "");
        parametros.put("FUNC_EMAIL", null != funcionario.getEmail() ? funcionario.getEmail() : "");
        parametros.put("FUNC_TELEFONE", null != funcionario.getTelefone() ? funcionario.getTelefone() : "");

        String endereco = null;
        String bairro = null;
        if (null != funcionario.getEndereco()) {
            endereco = funcionario.getEndereco().getLogradouro() + " N°" + funcionario.getEndereco().getNumero();
            bairro = funcionario.getEndereco().getBairro();

        }

        parametros.put("END_LOGRADOURO", null != endereco ? endereco : "");//RUA, NUMERO
        parametros.put("END_BAIRRO", null != bairro ? bairro : "");

        LocalDate now = LocalDate.now();
        String dtExame = now.getDayOfMonth() + "/" + now.getMonthValue() + "/" + now.getYear();
        parametros.put("DT_EXAME", dtExame);

        InputStream inputStream = this.getClass().getResourceAsStream("/relatorios/registro.jasper");

        final List<Empresa> empresas = funcionario.getEmpresas();

        JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream, parametros, new JRBeanCollectionDataSource(empresas));

        return JasperExportManager.exportReportToPdf(jasperPrint);
    }

    private void registraLinhasComErro(Map<Integer, String> linhasComErro, RowsImportDTO rowsImportDTO) {

        int cont = 1;
        List<String> values = new ArrayList<>();
        for (String item : linhasComErro.values()) {
            String msg = "{{ Erro ao importar a linha[ " + cont + " - " + item + " ] }}";
            values.add(msg);
            log.error(msg);
            cont++;
        }

        rowsImportDTO.setFalhas(values);
    }

    @Transactional
    public RowsImportDTO importaFuncionarios(MultipartFile reapExcelDataFile) {//NOSONAR

        Map<Integer, String> erros = new HashMap<>();
        Map<String, Empresa> empresaMap = this.empresaService.loadEmpresas();
        List<Funcionario> funcionarios = new ArrayList<>();

        RowsImportDTO rowsImport = new RowsImportDTO();
        int linha = 1;
        try {

            HSSFWorkbook workbook = new HSSFWorkbook(reapExcelDataFile.getInputStream());
            HSSFSheet sheetAtletas = workbook.getSheetAt(0);

            Iterator<Row> rowIterator = sheetAtletas.iterator();

            while (rowIterator.hasNext()) { //NOSONAR

                Row row = rowIterator.next();

                Cell cellCadastro = row.getCell(0);
                if (null == cellCadastro) {
                    erros.put(linha, "Erro no Numero de cadastro na linha: " + linha);
                    continue;
                }
                Number numeroCadastro = cellCadastro.getNumericCellValue();

                Cell cellNome = row.getCell(1);
                if (null == cellNome) {
                    erros.put(linha, "Erro no Nome do funcionario na linha: " + linha);
                    continue;
                }
                String nome = cellNome.getStringCellValue().toUpperCase().trim();
                if ("".equals(nome) || nome.length() < 3) {
                    erros.put(linha, "Erro no Nome do funcionario na linha: " + linha);
                    continue;
                }

                LocalDate dataNascimento;
                dataNascimento = this.calculaDataNascimento(row);

                Cell cellCpf = row.getCell(5);
                String cpf;
                String rg;

                cpf = validaCPF(cellCpf);

                if (null != cpf && cpf.length() == 11) {
                    rg = "";
                    final boolean matches = cpf.matches("[0-9]*");
                    if (!matches) {
                        rg = cpf;
                        cpf = "000.000.000-00";
                    }
                } else {
                    rg = cpf;
                    cpf = "000.000.000-00";
                }

                Cell cellNomeEmpresa = row.getCell(6);
                String nomeEmpresa = cellNomeEmpresa.getStringCellValue().trim();
                if ("".equals(nomeEmpresa)) {
                    erros.put(linha, "Erro no nome da empresa na linha " + linha);
                    continue;
                }

                Cell cellCnpj = row.getCell(7);
                if (null == cellCnpj) {
                    erros.put(linha, "Erro no CNPJ na linha " + linha);
                    continue;
                }

                String cnpj = cellCnpj.getStringCellValue().trim();
                cnpj = cnpj.replace(" ", "").replace(".", "").replace("-", "").replace("/", "");
                cnpj = cnpj.trim();
                if (cnpj.length() < 14) {
                    erros.put(linha, "Erro no CNPJ na linha " + linha);
                    continue;
                }

                Empresa empresa = getEmpresa(empresaMap, nomeEmpresa, cnpj);

                Funcionario func = new Funcionario(nome, rg, cpf, dataNascimento, "IMPORTAÇÃO", "IMPORTAÇÃO", numeroCadastro.longValue());
                func.setEmpresas(Arrays.asList(empresa));
                this.calculaIdade(func);

                funcionarios.add(func);

                linha++;
            }

        } catch (FileNotFoundException e) {
            log.error(e.getMessage());
            log.info("Arquivo Excel não encontrado!");
        } catch (Exception e) {
            erros.put(linha, e.getMessage());
            log.error(e.getMessage());
        }

        rowsImport.setTotalImportado(linha - 1);
        if (!erros.isEmpty()) {
            rowsImport.setTotalFalhas(erros.size());
            this.registraLinhasComErro(erros, rowsImport);
        }

        if (!funcionarios.isEmpty()) {
            this.insereEmBatch(funcionarios);
        }

        return rowsImport;

    }

    private Empresa getEmpresa(Map<String, Empresa> empresaMap, String nomeEmpresa, String cnpj) {
        Empresa empresa = null;
        if (empresaMap.containsKey(cnpj)) {
            empresa = empresaMap.get(cnpj);
        } else {
            try {
                empresa = this.empresaService.cadastraEmpresa(new Empresa(nomeEmpresa, cnpj));
                empresaMap.put(empresa.getCnpj(), empresa);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
        return empresa;
    }

    private String validaCPF(Cell cellCpf) {
        String cpf;
        try {
            cpf = cellCpf.getStringCellValue();

        } catch (IllegalStateException e) {
            Number val = cellCpf.getNumericCellValue();
            cpf = String.valueOf(val.longValue());
        } catch (Exception e) {
            cpf = "CPF-ilegivel";
        }
        cpf = cpf.replace(".", "").replace(":", "").replace("-", "").trim();
        return cpf;
    }

    private LocalDate calculaDataNascimento(Row row) {
        Date input;
        LocalDate dataNascimento;
        try {
            Cell cellNascimento = row.getCell(2);
            input = cellNascimento.getDateCellValue();
            dataNascimento = input.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        } catch (Exception e) {
            dataNascimento = LocalDate.now().minusYears(18);
        }
        return dataNascimento;
    }

    public List<FuncionarioDTO> buscaFuncionarioComAutoComplete(String nome) {

        List<FuncionarioDTO> funcionarios = new ArrayList<>();
        this.funcionarioRepository.findByNomeContainingIgnoreCaseOrderByNomeAscNumeroCadastroDesc(nome)
                .forEach(func -> {
                    String name = func.getNome();
                    String registro = func.getNumeroCadastro() != null ? func.getNumeroCadastro().toString() : "SEM N°";
                    String nomeFuncNum = name.concat(" - ").concat(registro);
                    func.setNomeFuncNum(nomeFuncNum);

                    funcionarios.add(new ModelMapper().map(func, FuncionarioDTO.class));
                });
        return funcionarios;
    }
}
