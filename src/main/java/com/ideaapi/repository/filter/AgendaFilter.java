package com.ideaapi.repository.filter;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

public class AgendaFilter {

    private String observacao;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate diaAgendaDe;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate diaAgendaAte;

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public LocalDate getDiaAgendaDe() {
        return diaAgendaDe;
    }

    public void setDiaAgendaDe(LocalDate diaAgendaDe) {
        this.diaAgendaDe = diaAgendaDe;
    }

    public LocalDate getDiaAgendaAte() {
        return diaAgendaAte;
    }

    public void setDiaAgendaAte(LocalDate diaAgendaAte) {
        this.diaAgendaAte = diaAgendaAte;
    }
}
