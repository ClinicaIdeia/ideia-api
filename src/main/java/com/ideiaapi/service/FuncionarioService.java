package com.ideiaapi.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

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
import com.ideiaapi.validate.FuncionarioValidate;

@Service
public class FuncionarioService {

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private FuncionarioValidate funcionarioValidate;

    @Autowired
    private FuncCargoEmpresaService funcCargoEmpresaService;

    @Autowired
    private S3 s3;

    public AnexoS3DTO salvarFotoFuncionarioS3(MultipartFile file) {
        String nome = s3.salvarArquivoS3Temporatimente(file, Boolean.TRUE);
        return new AnexoS3DTO(nome, s3.configuraUrl(nome));
    }

    public Page<Funcionario> filtrar(FuncionarioFilter filter, Pageable pageable) {
        Page<Funcionario> filtrar = this.funcionarioRepository.filtrar(filter, pageable);
        Usuario usuarioLogado = UsuarioSessao.getUserLogado();
        if (!UsuarioSessao.isAdmin(
                usuarioLogado) && (null != filtrar.getContent() && !filtrar.getContent().isEmpty())) {

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
        Funcionario salvo = this.funcionarioRepository.save(entity);
        this.funcCargoEmpresaService.insereCargo(salvo);
        return salvo;
    }

    public Funcionario buscaFuncionario(Long codigo) {
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
        final FuncCargoEmpresa funcCargoEmpresa = this.funcCargoEmpresaService.getByCodFuncionarioAndCodEmpresa(
                funcionario, userLogado.getEmpresa().getCodigo());

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
        } else if (StringUtils.hasText(
                funcionario.getAnexo()) && !funcionario.getAnexo().equals(funcionarioSalvo.getAnexo())) {
            this.s3.substituir(funcionarioSalvo.getAnexo(), funcionario.getAnexo());
        }
        BeanUtils.copyProperties(funcionario, funcionarioSalvo, "codigo");

        this.funcionarioValidate.fluxoAtualizacao(funcionarioSalvo);
        this.removeEmpresasDuplicadas(funcionarioSalvo);
        this.calculaIdade(funcionarioSalvo);
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
}
