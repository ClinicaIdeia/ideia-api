package com.ideiaapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ideiaapi.model.Motivo;

public interface MotivoRepository extends JpaRepository<Motivo, Long> {
}
