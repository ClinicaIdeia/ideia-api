package com.ideiaapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ideiaapi.model.Laudo;
import com.ideiaapi.repository.laudo.LaudoRepositoryQuery;

public interface LaudoRepository extends JpaRepository<Laudo, Long>, LaudoRepositoryQuery {
}
