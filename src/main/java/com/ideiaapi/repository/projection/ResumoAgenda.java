package com.ideiaapi.repository.projection;

import java.time.LocalDate;

public class ResumoAgenda {

    private Long codigo;
    private String observacao;
    private LocalDate diaAgenda;

    public ResumoAgenda(Long codigo, String observacao, LocalDate diaAgenda) {
        this.observacao = observacao;
        this.diaAgenda = diaAgenda;
        this.codigo = codigo;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public LocalDate getDiaAgenda() {
        return diaAgenda;
    }

    public void setDiaAgenda(LocalDate diaAgenda) {
        this.diaAgenda = diaAgenda;
    }

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }
}