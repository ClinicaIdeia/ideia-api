package com.ideaapi.repository.agenda;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ideaapi.model.Agenda;
import com.ideaapi.repository.filter.AgendaFilter;
import com.ideaapi.repository.filter.AgendamentoFilter;
import com.ideaapi.repository.projection.ResumoAgendamento;

public interface AgendaRepositoryQuery {

    Page<Agenda> filtrar(AgendaFilter agendaFilter, Pageable pageable);

    Page<ResumoAgendamento> resumir(AgendaFilter agendaFilter, Pageable pageable);

}
