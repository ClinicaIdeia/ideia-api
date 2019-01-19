package com.ideiaapi.repository.filter;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

public class LaudoFilter {

    private String observacao;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataLaudoDe;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataLaudoAte;

    private Long codFuncionario;

    private Long codEmpresa;

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public LocalDate getDataLaudoDe() {
        return dataLaudoDe;
    }

    public void setDataLaudoDe(LocalDate dataLaudoDe) {
        this.dataLaudoDe = dataLaudoDe;
    }

    public LocalDate getDataLaudoAte() {
        return dataLaudoAte;
    }

    public void setDataLaudoAte(LocalDate dataLaudoAte) {
        this.dataLaudoAte = dataLaudoAte;
    }

    public Long getCodFuncionario() {
        return codFuncionario;
    }

    public void setCodFuncionario(Long codFuncionario) {
        this.codFuncionario = codFuncionario;
    }

    public Long getCodEmpresa() {
        return codEmpresa;
    }

    public void setCodEmpresa(Long codEmpresa) {
        this.codEmpresa = codEmpresa;
    }
}
