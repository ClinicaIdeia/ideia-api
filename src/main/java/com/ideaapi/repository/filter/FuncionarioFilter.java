package com.ideaapi.repository.filter;

public class FuncionarioFilter {

    private String nome;

    private String matricula;

    private String rg;

    private String cpf;

    private String telefone;

    private Long codigoEmpresa;

    public Long getCodigoEmpresa() {
        return codigoEmpresa;
    }

    public void setCodigoEmpresa(Long codigoEmpresa) {
        this.codigoEmpresa = codigoEmpresa;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
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

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
}
