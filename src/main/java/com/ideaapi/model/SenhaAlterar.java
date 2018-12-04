package com.ideaapi.model;

import java.util.Objects;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

public class SenhaAlterar {

    @NotNull
    private String senhaNova;

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
