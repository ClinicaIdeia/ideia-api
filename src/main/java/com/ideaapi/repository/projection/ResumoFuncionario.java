package com.ideaapi.repository.projection;

public class ResumoFuncionario {

    private Long codigo;
    private String nome;
    private String matricula;
    private String rg;
    private String cpf;
    private String telefone;
    private String cargo;

    public ResumoFuncionario(Long codigo, String nome, String matricula, String rg, String cpf, String telefone,
            String cargo) {
        this.codigo = codigo;
        this.nome = nome;
        this.matricula = matricula;
        this.rg = rg;
        this.cpf = cpf;
        this.telefone = telefone;
        this.cargo = cargo;
    }

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

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }
}
