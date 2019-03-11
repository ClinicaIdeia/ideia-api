package com.ideiaapi.model;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "aptidao")
@SequenceGenerator(name = "aptidao_seq", sequenceName = "aptidao_seq", allocationSize = 1)
public class Aptidao {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "aptidao_seq")
    @Column(name = "CODIGO")
    private Long codigo;

    @Column(name = "DESCRICAO")
    private String descricao;

    @Column(name = "APTO")
    private Boolean apto;

    @Column(name = "INAPTO")
    private Boolean inapto;

    public Boolean getInapto() {
        return inapto;
    }

    public void setInapto(Boolean inapto) {
        this.inapto = inapto;
    }

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

    public Boolean getApto() {
        return apto;
    }

    public void setApto(Boolean apto) {
        this.apto = apto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Aptidao)) return false;
        Aptidao aptidao = (Aptidao) o;
        return Objects.equals(getCodigo(), aptidao.getCodigo()) &&
                Objects.equals(getDescricao(), aptidao.getDescricao()) &&
                Objects.equals(getApto(), aptidao.getApto());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getCodigo(), getDescricao(), getApto());
    }
    
}
