package com.ideiaapi.repository;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ideiaapi.model.Agendamento;
import com.ideiaapi.repository.agendamento.AgendamentoRepositoryQuery;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long>, AgendamentoRepositoryQuery {

    List<Agendamento> findAllByAgenda_DiaAgenda(LocalDate diaAgenda);

    List<Agendamento> findAllByAgenda_DiaAgenda_MonthAndAgenda_DiaAgenda_Year(Month month, Integer year);
}
