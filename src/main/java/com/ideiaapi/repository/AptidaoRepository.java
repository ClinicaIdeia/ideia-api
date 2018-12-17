package com.ideiaapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ideiaapi.model.Aptidao;

@Repository
public interface AptidaoRepository extends JpaRepository<Aptidao, Long> {
}
