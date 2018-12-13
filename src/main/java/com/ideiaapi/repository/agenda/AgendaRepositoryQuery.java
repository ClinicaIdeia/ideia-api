package com.ideiaapi.repository.agenda;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ideiaapi.model.Agenda;
import com.ideiaapi.repository.filter.AgendaFilter;
import com.ideiaapi.repository.projection.ResumoAgendamento;

public interface AgendaRepositoryQuery {

    Page<Agenda> filtrar(AgendaFilter agendaFilter, Pageable pageable);

    Page<ResumoAgendamento> resumir(AgendaFilter agendaFilter, Pageable pageable);

}
