package com.ideaapi.repository.agendamento;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ideaapi.model.Agendamento;
import com.ideaapi.repository.filter.AgendamentoFilter;
import com.ideaapi.repository.projection.ResumoAgendamento;

public interface AgendamentoRepositoryQuery {

    Page<Agendamento> filtrar(AgendamentoFilter agendaFilter, Pageable pageable);

    Page<ResumoAgendamento> resumir(AgendamentoFilter agendaFilter, Pageable pageable);

}
