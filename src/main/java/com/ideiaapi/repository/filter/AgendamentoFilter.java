package com.ideiaapi.repository.filter;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

public class AgendamentoFilter {

    private String observacao;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataExameDe;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataExameAte;

    private Boolean laudoGerado;

    public Boolean getLaudoGerado() {
        return laudoGerado;
    }

    public void setLaudoGerado(Boolean laudoGerado) {
        this.laudoGerado = laudoGerado;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public LocalDate getDataExameDe() {
        return dataExameDe;
    }

    public void setDataExameDe(LocalDate dataExameDe) {
        this.dataExameDe = dataExameDe;
    }

    public LocalDate getDataExameAte() {
        return dataExameAte;
    }

    public void setDataExameAte(LocalDate dataExameAte) {
        this.dataExameAte = dataExameAte;
    }
}
