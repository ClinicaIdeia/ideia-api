package com.ideaapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ideaapi.model.Horario;


public interface HorarioRepository extends JpaRepository<Horario, Long> {
}
