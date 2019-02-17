package com.ideiaapi.validate;

import static com.ideiaapi.constants.ErrorsCode.DIA_AGENDA_INFERIOR;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import com.ideiaapi.exceptions.BusinessException;
import com.ideiaapi.model.Agenda;

@Component
public class AgendaValidate {

    public void fluxoCriacao(Agenda entity) {
        this.validaDiaAgenda(entity);
    }

    public void fluxoAtualizacao(Agenda entity) {
        this.validaDiaAgenda(entity);
    }

    private void validaDiaAgenda(Agenda entity) {
        final LocalDate diaAgenda = entity.getDiaAgenda();
        if (null != diaAgenda) {
            if (diaAgenda.isBefore(LocalDate.now())) {
                throw new BusinessException(DIA_AGENDA_INFERIOR);
            }
        }

    }

}
