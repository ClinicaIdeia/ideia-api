package com.ideaapi.model;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "agendamento")
@SequenceGenerator(name = "agendamento_seq", sequenceName = "agendamento_seq", allocationSize = 1)
public class Agendamento {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "agendamento_seq")
    private Long codigo;

    @NotNull
    @OneToOne
    @JoinColumn(name = "codigo_agenda")
    private Agenda agenda;

    @NotNull
    @OneToOne
    @JoinColumn(name = "codigo_funcionario")
    private Funcionario funcionario;

    @NotNull
    @OneToOne
    @JoinColumn(name = "codigo_motivo")
    private Motivo motivo;

    @Transient
    private Long codHorario;

    public Long getCodHorario() {
        return codHorario;
    }

    public void setCodHorario(Long codHorario) {
        this.codHorario = codHorario;
    }

    public Motivo getMotivo() {
        return motivo;
    }

    public void setMotivo(Motivo motivo) {
        this.motivo = motivo;
    }

    private String observacao;

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
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

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Agendamento that = (Agendamento) o;
        return Objects.equals(codigo, that.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }
}
