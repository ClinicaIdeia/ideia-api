package com.ideiaapi.dto.s3;

public class AnexoS3DTO {

    private String nome;

    private String url;

    public AnexoS3DTO(String nome, String url) {
        this.nome = nome;
        this.url = url;
    }

    public String getNome() {
        return nome;
    }

    public String getUrl() {
        return url;
    }

}
