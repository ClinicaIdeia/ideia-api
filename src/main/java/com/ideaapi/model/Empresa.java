package com.ideaapi.model;

import java.util.List;
import java.util.Objects;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "empresa")
@SequenceGenerator(name = "empresa_seq", sequenceName = "empresa_seq", allocationSize = 1)
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "empresa_seq")
    private Long codigo;

    @NotNull
    @Size(min = 3, max = 100)
    private String nome;

    @NotNull
    @Size(min = 3, max = 20)
    private String cnpj;

    @ManyToMany
    @JoinTable(name = "empresa_contato", joinColumns = @JoinColumn(name = "codigo_empresa")
            , inverseJoinColumns = @JoinColumn(name = "codigo_contato"))
    private List<Contato> contatos;

    @Embedded
    private Endereco endereco;

    private Boolean ativa;

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

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public List<Contato> getContatos() {
        return contatos;
    }

    public void setContatos(List<Contato> contatos) {
        this.contatos = contatos;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public Boolean getAtiva() {
        return ativa;
    }

    public void setAtiva(Boolean ativa) {
        this.ativa = ativa;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Empresa empresa = (Empresa) o;
        return Objects.equals(codigo, empresa.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }

    @Override
    public String toString() {
        return "Empresa{" +
                "codigo=" + codigo +
                ", nome='" + nome + '\'' +
                ", cnpj='" + cnpj + '\'' +
                ", contatos=" + contatos +
                ", endereco=" + endereco +
                ", ativa=" + ativa +
                '}';
    }
}
