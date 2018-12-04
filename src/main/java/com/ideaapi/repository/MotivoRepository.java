package com.ideaapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ideaapi.model.Motivo;

public interface MotivoRepository extends JpaRepository<Motivo, Long> {
}
