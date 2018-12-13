package com.ideiaapi.repository.agendamento;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ideiaapi.model.Agendamento;
import com.ideiaapi.repository.filter.AgendamentoFilter;
import com.ideiaapi.repository.projection.ResumoAgendamento;

public interface AgendamentoRepositoryQuery {

    Page<Agendamento> filtrar(AgendamentoFilter agendaFilter, Pageable pageable);

    Page<ResumoAgendamento> resumir(AgendamentoFilter agendaFilter, Pageable pageable);

}
