package com.ideiaapi.dto;

import java.util.List;

import com.ideiaapi.model.Empresa;

public class FuncionarioDTO {

    private Long          codigo;
    private String        nome;
    private List<Empresa> empresas;
    private String nomeFuncNum;

    public FuncionarioDTO(Long codigo, String nome, List<Empresa> empresas, String nomeFuncNum) {
        this.codigo = codigo;
        this.nome = nome;
        this.empresas = empresas;
        this.nomeFuncNum = nomeFuncNum;
    }

    public List<Empresa> getEmpresas() {
        return empresas;
    }

    public void setEmpresas(List<Empresa> empresas) {
        this.empresas = empresas;
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

    public String getNomeFuncNum() {
        return nomeFuncNum;
    }

    public void setNomeFuncNum(String nomeFuncNum) {
        this.nomeFuncNum = nomeFuncNum;
    }
}
