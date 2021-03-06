package com.ideiaapi.dto;

public class EmpresaDTO {
    private Long codigo;
    private String nome;

    public EmpresaDTO() {
        super();
    }

    public EmpresaDTO(Long codigo, String nome) {
        this.codigo = codigo;
        this.nome = nome;
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
}
