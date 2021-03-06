package com.ideiaapi.model;

import java.time.LocalTime;
import java.util.Objects;

import javax.persistence.Column;
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
    @JoinColumn(name = "codigo_empresa")
    private Empresa empresa;

    @NotNull
    @OneToOne
    @JoinColumn(name = "codigo_motivo")
    private Motivo motivo;

    @Column( name = "HORA_EXAME")
    private LocalTime horaExame;

    @Transient
    private Long codHorario;

    @NotNull
    private Boolean trabalhoArmado;

    private Boolean avulso;

    private Boolean laudoGerado;

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public Boolean getLaudoGerado() {
        return laudoGerado;
    }

    public void setLaudoGerado(Boolean laudoGerado) {
        this.laudoGerado = laudoGerado;
    }

    public Boolean getAvulso() {
        return avulso;
    }

    public void setAvulso(Boolean avulso) {
        this.avulso = avulso;
    }

    public LocalTime getHoraExame() {
        return horaExame;
    }

    public void setHoraExame(LocalTime horaExame) {
        this.horaExame = horaExame;
    }

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

    public Boolean getTrabalhoArmado() {
        return trabalhoArmado;
    }

    public void setTrabalhoArmado(Boolean trabalhoArmado) {
        this.trabalhoArmado = trabalhoArmado;
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
