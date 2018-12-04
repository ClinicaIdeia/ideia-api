package com.ideaapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ideaapi.model.Agendamento;
import com.ideaapi.repository.agendamento.AgendamentoRepositoryQuery;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long>, AgendamentoRepositoryQuery {

}
