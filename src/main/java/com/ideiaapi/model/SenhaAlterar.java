package com.ideiaapi.model;

import java.util.Objects;

import javax.validation.constraints.NotNull;

public class SenhaAlterar {

    @NotNull
    private String senhaNova;

    private String confirmacao;

    public String getConfirmacao() {
        return confirmacao;
    }

    public void setConfirmacao(String confirmacao) {
        this.confirmacao = confirmacao;
    }

    public String getSenhaNova() {
        return senhaNova;
    }

    public void setSenhaNova(String senhaNova) {
        this.senhaNova = senhaNova;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SenhaAlterar)) return false;
        SenhaAlterar that = (SenhaAlterar) o;
        return Objects.equals(getSenhaNova(), that.getSenhaNova());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getSenhaNova());
    }

    @Override
    public String toString() {
        return "SenhaAlterar{" +
                "senhaNova='" + senhaNova + '\'' +
                '}';
    }
}
