package com.ideiaapi.model;

import java.util.Objects;

public class Exame {

    private String nomeFuncionario;
    private String dataExame;

    public Exame(String nomeFuncionario, String dataExame) {
        this.nomeFuncionario = nomeFuncionario;
        this.dataExame = dataExame;
    }

    public String getNomeFuncionario() {
        return nomeFuncionario;
    }

    public void setNomeFuncionario(String nomeFuncionario) {
        this.nomeFuncionario = nomeFuncionario;
    }

    public String getDataExame() {
        return dataExame;
    }

    public void setDataExame(String dataExame) {
        this.dataExame = dataExame;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Exame exame = (Exame) o;
        return Objects.equals(nomeFuncionario, exame.nomeFuncionario) &&
                Objects.equals(dataExame, exame.dataExame);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nomeFuncionario, dataExame);
    }
}
