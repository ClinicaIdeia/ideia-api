package com.ideiaapi.repository;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ideiaapi.model.Agendamento;
import com.ideiaapi.repository.agendamento.AgendamentoRepositoryQuery;
import org.springframework.stereotype.Repository;

@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, Long>, AgendamentoRepositoryQuery {

    List<Agendamento> findAllByAgendaDiaAgenda(LocalDate diaAgenda);

    List<Agendamento> findAllByAgendaDiaAgendaMonthAndAgendaDiaAgendaYear(Month month, Integer year);
}
