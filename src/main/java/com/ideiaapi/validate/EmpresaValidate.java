package com.ideiaapi.validate;

import static com.ideiaapi.constansts.ErrorsCode.CNPJ_DUPLICADO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ideiaapi.exceptions.BusinessException;
import com.ideiaapi.model.Empresa;
import com.ideiaapi.repository.EmpresaRepository;

@Component
public class EmpresaValidate {

    @Autowired
    private EmpresaRepository repository;

    public void fluxoCriacao(Empresa empresa) {
        this.validaCnpjUnique(empresa);
    }

    private void validaCnpjUnique(Empresa empresa) {

        Empresa saved = this.repository.findByCnpj(empresa.getCnpj());

        if (null != saved) {
            throw new BusinessException(CNPJ_DUPLICADO);
        }
    }

}
