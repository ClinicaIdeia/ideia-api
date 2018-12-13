package com.ideiaapi.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ideiaapi.model.Agenda;
import com.ideiaapi.repository.agenda.AgendaRepositoryQuery;
import com.ideiaapi.repository.agenda.AgendaRepositoryQuery;


public interface AgendaRepository extends JpaRepository<Agenda, Long>, AgendaRepositoryQuery {

    List<Agenda> findAllByDiaAgendaAfter(LocalDate localDate);

}
