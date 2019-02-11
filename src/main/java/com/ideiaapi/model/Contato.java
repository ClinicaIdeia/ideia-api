package com.ideiaapi.model;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;

@Entity
@Table(name = "contato")
@SequenceGenerator(name = "contato_seq", sequenceName = "contato_seq", allocationSize = 1)
public class Contato {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "contato_seq")
    private Long codigo;

    @NotNull
    @Size(min = 3, max = 50)
    private String nome;

    private String telefone;

    @Column(name = "TELEFONE_FIXO")
    private String telefoneFixo;

    @NotNull
    @Email
    @Size(min = 3, max = 50)
    private String email;

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getTelefoneFixo() {
        return telefoneFixo;
    }

    public void setTelefoneFixo(String telefoneFixo) {
        this.telefoneFixo = telefoneFixo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Contato)) return false;
        Contato contato = (Contato) o;
        return Objects.equals(getCodigo(), contato.getCodigo()) &&
                Objects.equals(getNome(), contato.getNome()) &&
                Objects.equals(getTelefone(), contato.getTelefone()) &&
                Objects.equals(getTelefoneFixo(), contato.getTelefoneFixo()) &&
                Objects.equals(getEmail(), contato.getEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }

    @Override
    public String toString() {
        return "Contato{" +
                "codigo=" + codigo +
                ", nome='" + nome + '\'' +
                ", telefone='" + telefone + '\'' +
                ", telefoneFixo='" + telefoneFixo + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
