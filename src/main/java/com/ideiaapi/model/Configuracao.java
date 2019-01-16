package com.ideiaapi.model;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "configuracao")
@SequenceGenerator(name = "configuracao_seq", sequenceName = "configuracao_seq", allocationSize = 1)
public class Configuracao {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "configuracao_seq")
    private Long codigo;

    @NotNull
    private String chave;

    @NotNull
    private String valor;

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public String getChave() {
        return chave;
    }

    public void setChave(String chave) {
        this.chave = chave;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Configuracao)) return false;
        Configuracao that = (Configuracao) o;
        return Objects.equals(getCodigo(), that.getCodigo()) &&
                Objects.equals(getChave(), that.getChave()) &&
                Objects.equals(getValor(), that.getValor());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getCodigo(), getChave(), getValor());
    }
}
