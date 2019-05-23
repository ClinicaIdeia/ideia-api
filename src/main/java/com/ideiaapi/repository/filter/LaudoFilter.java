package com.ideiaapi.repository.filter;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

public class LaudoFilter {

    private String observacao;

    private String motivo;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataAgendamentoDe;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataAgendamentoAte;

    private Long codFuncionario;

    private Long codEmpresa;

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public LocalDate getDataAgendamentoDe() {
        return dataAgendamentoDe;
    }

    public void setDataAgendamentoDe(LocalDate dataAgendamentoDe) {
        this.dataAgendamentoDe = dataAgendamentoDe;
    }

    public LocalDate getDataAgendamentoAte() {
        return dataAgendamentoAte;
    }

    public void setDataAgendamentoAte(LocalDate dataAgendamentoAte) {
        this.dataAgendamentoAte = dataAgendamentoAte;
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
