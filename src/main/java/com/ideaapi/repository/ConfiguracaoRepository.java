package com.ideaapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ideaapi.model.Configuracao;

public interface ConfiguracaoRepository extends JpaRepository<Configuracao, Long> {
}
