package com.ideiaapi.model;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.ideiaapi.enums.TipoAptidao;

@Entity
@Table(name = "aptidao")
@SequenceGenerator(name = "aptidao_seq", sequenceName = "aptidao_seq", allocationSize = 1)
public class Aptidao {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "aptidao_seq")
    @Column(name = "CODIGO")
    private Long codigo;

    @Column(name = "TIPO_APTIDAO")
    @Enumerated(EnumType.STRING)
    private TipoAptidao tipoAptidao;

    @Column(name = "APTO")
    private Boolean apto;

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public Boolean getApto() {
        return apto;
    }

    public void setApto(Boolean apto) {
        this.apto = apto;
    }

    public TipoAptidao getTipoAptidao() {
        return tipoAptidao;
    }

    public void setTipoAptidao(TipoAptidao tipoAptidao) {
        this.tipoAptidao = tipoAptidao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Aptidao aptidao = (Aptidao) o;
        return Objects.equals(codigo, aptidao.codigo) &&
                tipoAptidao == aptidao.tipoAptidao &&
                Objects.equals(apto, aptidao.apto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo, tipoAptidao, apto);
    }

    @Override
    public String toString() {
        return "Aptidao{" +
                "codigo=" + codigo +
                ", tipoAptidao=" + tipoAptidao +
                ", apto=" + apto +
                '}';
    }
}
