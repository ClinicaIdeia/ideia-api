package com.ideiaapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ideiaapi.model.Horario;

public interface HorarioRepository extends JpaRepository<Horario, Long> {
}
