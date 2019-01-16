package com.ideiaapi.model;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
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

import org.hibernate.validator.constraints.Email;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.ideiaapi.repository.listener.FuncionarioListener;
import com.ideiaapi.util.datas.LocalDateDeserializer;
import com.ideiaapi.util.datas.LocalDateSerializer;

@EntityListeners(FuncionarioListener.class)
@Entity
@Table(name = "funcionario")
@SequenceGenerator(name = "funcionario_seq", sequenceName = "funcionario_seq", allocationSize = 1)
public class Funcionario {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "funcionario_seq")
    @Column(name = "CODIGO")
    private Long codigo;

    @NotNull
    @Size(min = 3, max = 50)
    @Column(name = "NOME")
    private String nome;

    @Column(name = "RG")
    private String rg;

    @NotNull
    @Size(min = 3, max = 20)
    @Column(name = "CPF")
    private String cpf;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @NotNull
    @Column(name = "DATA_NASCIMENTO")
    private LocalDate dataNascimento;

    @Column(name = "SEXO")
    private String sexo;

    @Column(name = "ESTADO_CIVIL")
    private String estadoCivil;

    @Column(name = "ESCOLARIDADE")
    private String escolaridade;

    @Column(name = "NATURALIDADE")
    private String naturalidade;

    @Email
    @Column(name = "EMAIL")
    private String email;

    @Column(name = "MATRICULA")
    private String matricula;

    @Column(name = "CARGO")
    private String cargo;

    @NotNull
    @Size(min = 3, max = 20)
    @Column(name = "TELEFONE")
    private String telefone;

    @Embedded
    private Endereco endereco;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "funcionario_empresa", joinColumns = @JoinColumn(name = "codigo_funcionario")
            , inverseJoinColumns = @JoinColumn(name = "codigo_empresa"))
    private List<Empresa> empresas;

    private Integer idade;

    private String anexo;

    @Transient
    private String urlAnexo;

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

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getEstadoCivil() {
        return estadoCivil;
    }

    public void setEstadoCivil(String estadoCivil) {
        this.estadoCivil = estadoCivil;
    }

    public String getEscolaridade() {
        return escolaridade;
    }

    public void setEscolaridade(String escolaridade) {
        this.escolaridade = escolaridade;
    }

    public String getNaturalidade() {
        return naturalidade;
    }

    public void setNaturalidade(String naturalidade) {
        this.naturalidade = naturalidade;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public List<Empresa> getEmpresas() {
        return empresas;
    }

    public void setEmpresas(List<Empresa> empresas) {
        this.empresas = empresas;
    }

    public Integer getIdade() {
        return idade;
    }

    public void setIdade(Integer idade) {
        this.idade = idade;
    }

    public String getAnexo() {
        return anexo;
    }

    public void setAnexo(String anexo) {
        this.anexo = anexo;
    }

    public String getUrlAnexo() {
        return urlAnexo;
    }

    public void setUrlAnexo(String urlAnexo) {
        this.urlAnexo = urlAnexo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Funcionario that = (Funcionario) o;
        return Objects.equals(codigo, that.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }

    @Override
    public String toString() {
        return "Funcionario{" +
                "codigo=" + codigo +
                ", nome='" + nome + '\'' +
                ", rg='" + rg + '\'' +
                ", cpf='" + cpf + '\'' +
                ", dataNascimento=" + dataNascimento +
                ", sexo='" + sexo + '\'' +
                ", estadoCivil='" + estadoCivil + '\'' +
                ", escolaridade='" + escolaridade + '\'' +
                ", naturalidade='" + naturalidade + '\'' +
                ", email='" + email + '\'' +
                ", matricula='" + matricula + '\'' +
                ", cargo='" + cargo + '\'' +
                ", telefone='" + telefone + '\'' +
                ", endereco=" + endereco +
                ", empresas=" + empresas +
                ", idade=" + idade +
                ", anexo='" + anexo + '\'' +
                ", urlAnexo='" + urlAnexo + '\'' +
                '}';
    }
}
