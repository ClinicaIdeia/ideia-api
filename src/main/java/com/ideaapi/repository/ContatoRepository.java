package com.ideaapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ideaapi.model.Contato;


public interface ContatoRepository extends JpaRepository<Contato    , Long> {
}
