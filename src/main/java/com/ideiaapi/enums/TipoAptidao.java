package com.ideiaapi.enums;

public enum TipoAptidao {

    MANUSEIO_ARMA("Manuseio de arma de fogo"),
    MANUSEIO_ARMA_EXE_PROFISSAO("Manuseio de arma de fogo e ao exercício da profissão de vigilante");

    public String descricao;

    TipoAptidao(String descricao) {
        this.descricao = descricao;
    }

}
