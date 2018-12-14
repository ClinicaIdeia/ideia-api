package com.ideiaapi.dto;

import java.time.LocalTime;

import com.ideiaapi.model.Agenda;
import com.ideiaapi.model.Funcionario;
import com.ideiaapi.model.Motivo;

public class AgendamentoEstatisticaEmpresa {

    private Agenda agenda;

    private Funcionario funcionario;

    private Motivo motivo;

    private LocalTime horaExame;

    public AgendamentoEstatisticaEmpresa(Agenda agenda, Funcionario funcionario, Motivo motivo,
            LocalTime horaExame) {
        this.agenda = agenda;
        this.funcionario = funcionario;
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
}
