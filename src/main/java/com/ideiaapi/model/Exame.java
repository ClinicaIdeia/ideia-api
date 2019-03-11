package com.ideiaapi.model;

import java.time.LocalDate;
import java.util.Objects;

public class Exame {

    public Exame(String nomeFuncionario, LocalDate dataExame) {
        this.nomeFuncionario = nomeFuncionario;
        this.dataExame = dataExame;
    }

    private String nomeFuncionario;

    private LocalDate dataExame;

    public String getNomeFuncionario() {
        return nomeFuncionario;
    }

    public void setNomeFuncionario(String nomeFuncionario) {
        this.nomeFuncionario = nomeFuncionario;
    }

    public LocalDate getDataExame() {
        return dataExame;
    }

    public void setDataExame(LocalDate dataExame) {
        this.dataExame = dataExame;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Exame)) return false;
        Exame exame = (Exame) o;
        return Objects.equals(getNomeFuncionario(), exame.getNomeFuncionario()) &&
                Objects.equals(getDataExame(), exame.getDataExame());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getNomeFuncionario(), getDataExame());
    }
}
