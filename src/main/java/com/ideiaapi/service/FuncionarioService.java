package com.ideiaapi.service;

import java.time.LocalDate;

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
import com.ideiaapi.model.Funcionario;
import com.ideiaapi.repository.FuncionarioRepository;
import com.ideiaapi.repository.filter.FuncionarioFilter;
import com.ideiaapi.repository.projection.ResumoFuncionario;
import com.ideiaapi.storage.S3;
import com.ideiaapi.validate.FuncionarioValidate;

@Service
public class FuncionarioService {

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private FuncionarioValidate funcionarioValidate;

    @Autowired
    private S3 s3;

    public AnexoS3DTO salvarFotoFuncionarioS3(MultipartFile file) {
        String nome = s3.salvarArquivoS3Temporatimente(file);
        return new AnexoS3DTO(nome, s3.configuraUrl(nome));
    }

    public Page<Funcionario> filtrar(FuncionarioFilter filter, Pageable pageable) {
        return this.funcionarioRepository.filtrar(filter, pageable);

    }

    public Page<ResumoFuncionario> resumo(FuncionarioFilter filter, Pageable pageable) {
        return this.funcionarioRepository.resumir(filter, pageable);
    }

    public Funcionario cadastraFuncionario(Funcionario entity) {

        this.funcionarioValidate.fluxoCriacao(entity);
        if (StringUtils.hasText(entity.getAnexo())) {
            this.s3.salvar(entity.getAnexo());
        }
        this.calculaIdade(entity);
        return this.funcionarioRepository.save(entity);
    }

    public Funcionario buscaFuncionario(Long codigo) {
        Funcionario funcionario = this.funcionarioRepository.findOne(codigo);

        if (funcionario == null) {
            throw new EmptyResultDataAccessException(1);
        }

        return funcionario;
    }

    public void deletaFuncionario(Long codigo) {
        this.funcionarioRepository.delete(codigo);
    }

    public ResponseEntity<Funcionario> atualizaFuncionario(Long codigo, Funcionario funcionario) {
        Funcionario funcionarioSalvo = this.buscaFuncionario(codigo);

        if (StringUtils.isEmpty(funcionario.getAnexo()) && StringUtils.hasText(funcionarioSalvo.getAnexo())) {
            this.s3.remover(funcionarioSalvo.getAnexo());
        } else if (StringUtils.hasText(
                funcionario.getAnexo()) && !funcionario.getAnexo().equals(funcionarioSalvo.getAnexo())) {
            this.s3.substituir(funcionarioSalvo.getAnexo(), funcionario.getAnexo());
        }
        BeanUtils.copyProperties(funcionario, funcionarioSalvo, "codigo");

        this.calculaIdade(funcionarioSalvo);
        this.funcionarioRepository.save(funcionarioSalvo);
        return ResponseEntity.ok(funcionarioSalvo);
    }

    private void calculaIdade(Funcionario funcionario) {
        final int yearNow = LocalDate.now().getYear();
        final int nascimento = funcionario.getDataNascimento().getYear();
        funcionario.setIdade(yearNow - nascimento);
    }
}
