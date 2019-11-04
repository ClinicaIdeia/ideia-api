package com.ideiaapi.dto;

import java.time.LocalDateTime;
import java.time.LocalTime;

import com.ideiaapi.model.Agenda;
import com.ideiaapi.model.Empresa;
import com.ideiaapi.model.Funcionario;
import com.ideiaapi.model.Motivo;
import java.time.LocalDate;

public class AgendamentoEstatisticaEmpresa {

    private Agenda agenda;

    private Funcionario funcionario;

    private Empresa empresa;

    private Motivo motivo;

    private LocalTime horaExame;

    private LocalDateTime dtaTemp;

    private String nomeEmpresaTemp;

    public AgendamentoEstatisticaEmpresa(Agenda agenda, Funcionario funcionario, Empresa empresa,
            Motivo motivo, LocalTime horaExame) {
        this.agenda = agenda;
        this.funcionario = funcionario;
        this.empresa = empresa;
        this.motivo = motivo;
        this.horaExame = horaExame;
    }

    public Agenda getAgenda() {
        return agenda;
    }

    public void setAgenda(Agenda agenda) {
        this.agenda = agenda;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public Motivo getMotivo() {
        return motivo;
    }

    public void setMotivo(Motivo motivo) {
        this.motivo = motivo;
    }

    public LocalTime getHoraExame() {
        return horaExame;
    }

    public void setHoraExame(LocalTime horaExame) {
        this.horaExame = horaExame;
    }

    public LocalDateTime getDtaTemp() {
        return dtaTemp;
    }

    public void setDtaTemp(LocalDateTime dtaTemp) {
        this.dtaTemp = dtaTemp;
    }

    public String getNomeEmpresaTemp() {
        return nomeEmpresaTemp;
    }

    public void setNomeEmpresaTemp(String nomeEmpresaTemp) {
        this.nomeEmpresaTemp = nomeEmpresaTemp;
    }
}
