package com.ideiaapi.dto;

public class AptidaoDTO {

    private String apto;
    private String descricao;

    public AptidaoDTO(String apto, String descricao) {
        this.apto = apto;
        this.descricao = descricao;
    }

    public String getApto() {
        return apto;
    }

    public void setApto(String apto) {
        this.apto = apto;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
