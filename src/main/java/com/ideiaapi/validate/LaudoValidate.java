package com.ideiaapi.validate;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import com.ideiaapi.model.Laudo;

@Component
public class LaudoValidate {

    public void fluxoCriacao(Laudo laudo) {
        this.validadeDatas(laudo);
    }

    private void validadeDatas(Laudo laudo) {

        laudo.setDataAtualizacao(LocalDate.now());
        if (null == laudo.getDataCriacao()) {
            laudo.setDataCriacao(LocalDate.now());
        }
        if (null == laudo.getDataEmissao()) {
            laudo.setDataEmissao(LocalDate.now());
        }
        if (null == laudo.getDataExame()) {
            laudo.setDataExame(LocalDate.now());
        }
    }

}
