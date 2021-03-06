package com.ideiaapi.model;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "agenda")
@SequenceGenerator(name = "agenda_seq", sequenceName = "agenda_seq", allocationSize = 1)
public class Agenda {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "agenda_seq")
    private Long codigo;

    @NotNull
    private LocalDate diaAgenda;

    @NotNull
    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "agenda_horario", joinColumns = @JoinColumn(name = "codigo_agenda")
            , inverseJoinColumns = @JoinColumn(name = "codigo_horario"))
    private List<Horario> horarios;

    private String observacao;

    @Transient
    private List<LocalDate> diasCopia;

    @Transient
    private String dataAgendaTemp;

    public String getDataAgendaTemp() {
        return dataAgendaTemp;
    }

    public void setDataAgendaTemp(String dataAgendaTemp) {
        this.dataAgendaTemp = dataAgendaTemp;
    }

    public List<LocalDate> getDiasCopia() {
        return diasCopia;
    }

    public void setDiasCopia(List<LocalDate> diasCopia) {
        this.diasCopia = diasCopia;
    }

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public LocalDate getDiaAgenda() {
        return diaAgenda;
    }

    public void setDiaAgenda(LocalDate diaAgenda) {
        this.diaAgenda = diaAgenda;
    }

    public List<Horario> getHorarios() {
        return horarios;
    }

    public void setHorarios(List<Horario> horarios) {
        this.horarios = horarios;
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
        Agenda that = (Agenda) o;
        return Objects.equals(codigo, that.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }

}
