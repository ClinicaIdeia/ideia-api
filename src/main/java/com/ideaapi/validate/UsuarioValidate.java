package com.ideaapi.validate;

import static com.ideaapi.constansts.ErrorsCode.CNPJ_DUPLICADO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ideaapi.exceptions.BusinessException;
import com.ideaapi.model.Empresa;
import com.ideaapi.repository.EmpresaRepository;

@Component
public class UsuarioValidate {

    @Autowired
    private EmpresaRepository repository;

    public void validate(Empresa empresa) {
        this.validaCnpjUnique(empresa);
    }

    private void validaCnpjUnique(Empresa empresa) {

        Empresa saved = this.repository.findByCnpj(empresa.getCnpj());

        if (null != saved) {
            throw new BusinessException(CNPJ_DUPLICADO);
        }
    }

}
