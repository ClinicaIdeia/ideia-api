package com.ideiaapi.validate;

import static com.ideiaapi.constants.ErrorsCode.CNPJ_DUPLICADO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ideiaapi.exceptions.BusinessException;
import com.ideiaapi.model.Laudo;
import com.ideiaapi.repository.LaudoRepository;

@Component
public class LaudoValidate {

    @Autowired
    private LaudoRepository repository;

    public void fluxoCriacao(Laudo laudo) {
//        this.validaCnpjUnique(laudo);
    }

//    private void validaCnpjUnique(Laudo laudo) {
//
//        Laudo saved = this.repository.findByCnpj(laudo.getCnpj());
//
//        if (null != saved) {
//            throw new BusinessException(CNPJ_DUPLICADO);
//        }
//    }

}
