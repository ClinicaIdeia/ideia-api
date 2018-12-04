package com.ideaapi.repository.restricoes.paginacao;

import javax.persistence.TypedQuery;

import org.springframework.data.domain.Pageable;

public class RestricoesPaginacao {

    protected void adicionarRestricoesDePaginacao(TypedQuery<?> query, Pageable pageable) {

        int paginaAtual = pageable.getPageNumber();
        int totalRegistrosPorPagina = pageable.getPageSize();
        int primeiroRegistroDaPagina = paginaAtual * totalRegistrosPorPagina;

        query.setFirstResult(primeiroRegistroDaPagina);
        query.setMaxResults(totalRegistrosPorPagina);

    }

}
