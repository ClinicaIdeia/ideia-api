package com.ideiaapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ideiaapi.model.Contato;

public interface ContatoRepository extends JpaRepository<Contato, Long> {
}
