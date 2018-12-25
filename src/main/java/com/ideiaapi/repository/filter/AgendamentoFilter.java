package com.ideiaapi.repository.filter;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

public class AgendamentoFilter {

    private String observacao;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataExameDe;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataExameAte;

    private Long codigoEmpresa;

    public Long getCodigoEmpresa() {
        return codigoEmpresa;
    }

    public void setCodigoEmpresa(Long codigoEmpresa) {
        this.codigoEmpresa = codigoEmpresa;
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
