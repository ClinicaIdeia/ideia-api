package com.ideaapi.model;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "horario")
@SequenceGenerator(name = "horario_seq", sequenceName = "horario_seq", allocationSize = 1)
public class Horario {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "horario_seq")
    private Long codigo;

    private String horaExame;

    private Integer maximoPermitido;

    private Integer restante;

    private Boolean disponivel;

    private Boolean avulso;

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public String getHoraExame() {
        return horaExame;
    }

    public void setHoraExame(String horaExame) {
        this.horaExame = horaExame;
    }

    public Integer getMaximoPermitido() {
        return maximoPermitido;
    }

    public void setMaximoPermitido(Integer maximoPermitido) {
        this.maximoPermitido = maximoPermitido;
    }

    public Boolean getDisponivel() {
        return disponivel;
    }

    public void setDisponivel(Boolean disponivel) {
        this.disponivel = disponivel;
    }

    public Boolean getAvulso() {
        return avulso;
    }

    public void setAvulso(Boolean avulso) {
        this.avulso = avulso;
    }

    public Integer getRestante() {
        return restante;
    }

    public void setRestante(Integer restante) {
        this.restante = restante;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Horario horario = (Horario) o;
        return Objects.equals(codigo, horario.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }

    @Override
    public String toString() {
        return "Horario{" +
                "codigo=" + codigo +
                ", horaExame='" + horaExame + '\'' +
                ", maximoPermitido=" + maximoPermitido +
                ", disponivel=" + disponivel +
                ", avulso=" + avulso +
                '}';
    }
}
