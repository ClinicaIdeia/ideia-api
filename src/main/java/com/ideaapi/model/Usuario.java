package com.ideaapi.model;


import java.util.List;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;

@Entity
@Table(name = "usuario")
@SequenceGenerator(name = "usuario_seq", sequenceName = "usuario_seq", allocationSize = 1)
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usuario_seq")
    private Long codigo;

    @NotNull
    @Size(min = 3, max = 50)
    private String nome;

    @NotNull
    @Email
    @Size(min = 3, max = 50)
    private String email;

    private String senha;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "codigo_empresa")
    private Empresa empresa;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "usuario_permissao", joinColumns = @JoinColumn(name = "codigo_usuario")
            , inverseJoinColumns = @JoinColumn(name = "codigo_permissao"))
    private List<Permissao> permissoes;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public List<Permissao> getPermissoes() {
        return permissoes;
    }

    public void setPermissoes(List<Permissao> permissoes) {
        this.permissoes = permissoes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(codigo, usuario.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "codigo=" + codigo +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", senha='" + senha + '\'' +
                ", empresa=" + empresa +
                ", permissoes=" + permissoes +
                '}';
    }
}