package com.ideiaapi.validate;

import static com.ideiaapi.constants.ErrorsCode.CPF_DUPLICADO;
import static com.ideiaapi.constants.ErrorsCode.DATA_NASCIMENTO_SUPERIOR;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ideiaapi.exceptions.BusinessException;
import com.ideiaapi.model.Funcionario;
import com.ideiaapi.repository.FuncionarioRepository;

@Component
public class FuncionarioValidate {

    @Autowired
    private FuncionarioRepository repository;

    public void fluxoCriacao(Funcionario entity) {
//        this.validaCpfUnique(entity);
        this.validaDataAniversario(entity);
    }

    public void fluxoAtualizacao(Funcionario entity) {
        this.validaDataAniversario(entity);
    }

    private void validaDataAniversario(Funcionario entity) {
        final LocalDate dataNascimento = entity.getDataNascimento();
        if (null != dataNascimento) {
            if (dataNascimento.isAfter(LocalDate.now())) {
                throw new BusinessException(DATA_NASCIMENTO_SUPERIOR);
            }
        }

    }

    private void validaCpfUnique(Funcionario funcionario) {

        Funcionario saved = this.repository.findByCpf(funcionario.getCpf());

        if (null != saved) {
            throw new BusinessException(CPF_DUPLICADO);
        }
    }
}
