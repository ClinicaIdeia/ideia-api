package com.ideaapi.model;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "motivo")
@SequenceGenerator(name = "motivo_seq", sequenceName = "motivo_seq", allocationSize = 1)
public class Motivo {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "motivo_seq")
    private Long codigo;

    private String descricao;

    private String observacao;

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
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
        Motivo motivo = (Motivo) o;
        return Objects.equals(codigo, motivo.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }
}
