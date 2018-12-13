package com.ideiaapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ideiaapi.model.Configuracao;

public interface ConfiguracaoRepository extends JpaRepository<Configuracao, Long> {
}
