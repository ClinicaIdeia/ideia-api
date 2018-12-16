package com.ideiaapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ideiaapi.model.Laudo;

public interface LaudoRepository extends JpaRepository<Laudo, Long> {
}
