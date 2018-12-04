package com.ideaapi.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ideaapi.model.Agenda;
import com.ideaapi.repository.agenda.AgendaRepositoryQuery;


public interface AgendaRepository extends JpaRepository<Agenda, Long>, AgendaRepositoryQuery {

    List<Agenda> findAllByDiaAgendaAfter(LocalDate localDate);

}
