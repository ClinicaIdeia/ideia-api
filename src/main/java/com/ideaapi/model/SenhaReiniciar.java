package com.ideaapi.model;

import java.util.Objects;

import org.hibernate.validator.constraints.NotBlank;

public class SenhaReiniciar {

    @NotBlank
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SenhaReiniciar)) return false;
        SenhaReiniciar that = (SenhaReiniciar) o;
        return Objects.equals(getEmail(), that.getEmail());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getEmail());
    }

    @Override
    public String toString() {
        return "SenhaReiniciar{" +
                "email='" + email + '\'' +
                '}';
    }
}
