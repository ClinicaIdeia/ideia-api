package com.ideiaapi.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "func_cargo_empresa")
@SequenceGenerator(name = "func_cargo_empresa_seq", sequenceName = "func_cargo_empresa_seq", allocationSize = 1)
public class FuncCargoEmpresa implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "func_cargo_empresa_seq")
    private Long codigo;

    private String cargo;

    private LocalDateTime dataCriacao;

    private LocalDateTime dataAtualizacao;

    private Long codFuncionario;

    private Long codEmpresa;

    public FuncCargoEmpresa() {

    }

    public FuncCargoEmpresa(String cargo, LocalDateTime dataCriacao, LocalDateTime dataAtualizacao,
            Long codFuncionario, Long codEmpresa) {
        this.cargo = cargo;
        this.dataCriacao = dataCriacao;
        this.dataAtualizacao = dataAtualizacao;
        this.codFuncionario = codFuncionario;
        this.codEmpresa = codEmpresa;
    }

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FuncCargoEmpresa that = (FuncCargoEmpresa) o;
        return Objects.equals(codFuncionario, that.codFuncionario) &&
                Objects.equals(codEmpresa, that.codEmpresa);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codFuncionario, codEmpresa);
    }
}
