package com.ideiaapi.service;

import static com.ideiaapi.constants.ErrorsCode.LAUDO_NAO_ENCONTRADO;

import java.io.InputStream;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ideiaapi.dto.AptidaoDTO;
import com.ideiaapi.exceptions.BusinessException;
import com.ideiaapi.model.Agendamento;
import com.ideiaapi.model.Funcionario;
import com.ideiaapi.model.Laudo;
import com.ideiaapi.repository.LaudoRepository;
import com.ideiaapi.repository.filter.LaudoFilter;
import com.ideiaapi.util.datas.UtilsData;
import com.ideiaapi.validate.LaudoValidate;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
public class LaudoService {

    @Autowired
    private LaudoRepository laudoRepository;

    @Autowired
    private AptidaoService aptidaoService;

    @Autowired
    private LaudoValidate laudoValidate;

    @Autowired
    private AgendamentoService agendamentoService;

    public byte[] imprimeLaudo(Long codigo) throws Exception {
        Laudo laudo = this.buscaLaudo(codigo);

        if (null == laudo) {
            throw new BusinessException(LAUDO_NAO_ENCONTRADO);
        }

        if (laudo.getAgendamento().getTrabalhoArmado()) {
            return this.laudoTrabalhoArmado(laudo);
        }

        return this.atestadoPorFuncionario(laudo);
    }

    private byte[] laudoTrabalhoArmado(Laudo laudo) throws Exception {

        Map<String, Object> parametros = new HashMap<>();

        final Funcionario funcionario = laudo.getFuncionario();

        parametros.put("FUNC_NOME", funcionario.getNome());
        parametros.put("FUNC_SEXO", funcionario.getSexo());
        parametros.put("FUNC_ESTADO_CIVIL", funcionario.getEstadoCivil());
        parametros.put("FUNC_IDADE", String.valueOf(funcionario.getIdade()));
        parametros.put("FUNC_ESCOLARIDADE", funcionario.getEscolaridade());
        parametros.put("FUNC_CPF", funcionario.getCpf());
        parametros.put("FUNC_PROFISSAO", funcionario.getCargo());
        parametros.put("DT_AVALIACAO", Date.valueOf(laudo.getDataExame()));
        parametros.put("DATA_EMISSAO", UtilsData.getDataFormatadaRecibo(laudo.getDataExame()));

        InputStream inputStream = this.getClass().getResourceAsStream("/relatorios/laudo.jasper");

        List<AptidaoDTO> aptidoes = new ArrayList<>();

        laudo.getAptidoes().forEach(aptidao ->
                aptidoes.add(new AptidaoDTO(aptidao.getApto() ? "( X )" : "(   )", aptidao.getDescricao()))
        );

        JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream, parametros,
                new JRBeanCollectionDataSource(aptidoes));

        return JasperExportManager.exportReportToPdf(jasperPrint);
    }

    private byte[] atestadoPorFuncionario(Laudo laudo) throws Exception {

        Map<String, Object> parametros = new HashMap<>();

        final Funcionario funcionario = laudo.getFuncionario();

        parametros.put("FUNC_NOME", funcionario.getNome());
        parametros.put("FUNC_SEXO", funcionario.getDataNascimento());
        parametros.put("FUNC_CPF", funcionario.getCpf());
        parametros.put("FUNC_PROFISSAO", funcionario.getCargo());
        parametros.put("DT_AVALIACAO", Date.valueOf(laudo.getDataExame()));
        parametros.put("DT_NASCIMENTO", Date.valueOf(funcionario.getDataNascimento()));
        parametros.put("DATA_EMISSAO", UtilsData.getDataFormatadaRecibo(laudo.getDataExame()));

        InputStream inputStream = this.getClass().getResourceAsStream("/relatorios/atestado.jasper");

        //TODO refatorar
//        JasperReport jasperReport = JasperFillManager.fillReport()

        JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream, parametros,
                new JRBeanCollectionDataSource(Arrays.asList(laudo.getAptidoes().stream().findFirst().get())));

        return JasperExportManager.exportReportToPdf(jasperPrint);
    }


    public Page<Laudo> filtrar(LaudoFilter filter, Pageable pageable) {
        Page<Laudo> laudos = this.laudoRepository.filtrar(filter, pageable);
        laudos.iterator().forEachRemaining(laudo -> {
            final Funcionario funcionario = laudo.getFuncionario();
            String nomeFuncNum = funcionario.getNome() + " - " + funcionario.getNumeroCadastro();
            laudo.getFuncionario().setNomeFuncNum(nomeFuncNum);
        });
        return laudos;
    }

    public Laudo cadastraLaudo(Laudo entity) {
        this.laudoValidate.fluxoCriacao(entity);

        this.aptidaoService.cadastrarAptidoes(entity.getAptidoes());

        final Laudo save = this.laudoRepository.save(entity);
        this.agendamentoService.marcarLaudoGerado(save);
        return save;
    }

    public Laudo buscaLaudo(Long codigo) {
        Laudo laudo = this.laudoRepository.findOne(codigo);
        if (laudo != null) {
            return laudo;
        }

        return null;
    }

    public void deletaLaudo(Long codigo) {
        Laudo laudo = this.buscaLaudo(codigo);
        this.reativaAgendamentoRelacionado(laudo);
        this.laudoRepository.delete(codigo);
    }

    private void reativaAgendamentoRelacionado(Laudo laudo) {
        agendamentoService.removeLaudoGeradoAgendamento(laudo.getAgendamento());
    }

    public ResponseEntity<Laudo> atualizaLaudo(Long codigo, Laudo laudo) {
        Laudo laudoSalva = this.buscaLaudo(codigo);
        BeanUtils.copyProperties(laudo, laudoSalva, "codigo");

        this.laudoRepository.save(laudoSalva);
        return ResponseEntity.ok(laudoSalva);
    }
}
