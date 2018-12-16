package com.ideiaapi.model;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "laudo")
@SequenceGenerator(name = "laudo_seq", sequenceName = "laudo_seq", allocationSize = 1)
public class Laudo {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "laudo_seq")
    @Column(name = "CODIGO")
    private Long codigo;

    @NotNull
    @OneToOne
    @JoinColumn(name = "codigo_funcionario")
    private Funcionario funcionario;

    @NotNull
    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "laudo_aptidao", joinColumns = @JoinColumn(name = "codigo_laudo")
            , inverseJoinColumns = @JoinColumn(name = "codigo_aptidao"))
    private List<Aptidao> aptidoes;

    @Column(name = "OBSERVACAO")
    private String observacao;

    @NotNull
    @OneToOne
    @JoinColumn(name = "codigo_motivo")
    private Motivo motivo;

    @Column(name = "DATA_EXAME")
    private LocalDate dataExame;

    @Column(name = "DATA_EMISSAO")
    private LocalDate dataEmissao;

    @Column(name = "DATA_CRICAO")
    private LocalDate dataCriacao;

    @Column(name = "DATA_ATUALIZACAO")
    private LocalDate dataAtualizacao;

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public List<Aptidao> getAptidoes() {
        return aptidoes;
    }

    public void setAptidoes(List<Aptidao> aptidoes) {
        this.aptidoes = aptidoes;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Motivo getMotivo() {
        return motivo;
    }

    public void setMotivo(Motivo motivo) {
        this.motivo = motivo;
    }

    public LocalDate getDataExame() {
        return dataExame;
    }

    public void setDataExame(LocalDate dataExame) {
        this.dataExame = dataExame;
    }

    public LocalDate getDataEmissao() {
        return dataEmissao;
    }

    public void setDataEmissao(LocalDate dataEmissao) {
        this.dataEmissao = dataEmissao;
    }

    public LocalDate getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDate dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public LocalDate getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(LocalDate dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Laudo)) return false;
        Laudo laudo = (Laudo) o;
        return Objects.equals(getCodigo(), laudo.getCodigo()) &&
                Objects.equals(getFuncionario(), laudo.getFuncionario()) &&
                Objects.equals(getAptidoes(), laudo.getAptidoes()) &&
                Objects.equals(getObservacao(), laudo.getObservacao()) &&
                Objects.equals(getMotivo(), laudo.getMotivo()) &&
                Objects.equals(getDataExame(), laudo.getDataExame()) &&
                Objects.equals(getDataEmissao(), laudo.getDataEmissao()) &&
                Objects.equals(getDataCriacao(), laudo.getDataCriacao()) &&
                Objects.equals(getDataAtualizacao(), laudo.getDataAtualizacao());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getCodigo(), getFuncionario(), getAptidoes(), getObservacao(), getMotivo(), getDataExame(),
                getDataEmissao(), getDataCriacao(), getDataAtualizacao());
    }

    @Override
    public String toString() {
        return "Laudo{" +
                "codigo=" + codigo +
                ", funcionario=" + funcionario +
                ", aptidoes=" + aptidoes +
                ", observacao='" + observacao + '\'' +
                ", motivo=" + motivo +
                ", dataExame=" + dataExame +
                ", dataEmissao=" + dataEmissao +
                ", dataCriacao=" + dataCriacao +
                ", dataAtualizacao=" + dataAtualizacao +
                '}';
    }
}

