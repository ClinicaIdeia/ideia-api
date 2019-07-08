package com.ideiaapi.repository.agendamento;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ideiaapi.dto.AgendamentoEstatisticaEmpresa;
import com.ideiaapi.model.Agendamento;
import com.ideiaapi.repository.filter.AgendamentoFilter;
import com.ideiaapi.repository.projection.ResumoAgendamento;

public interface AgendamentoRepositoryQuery {

    Set<AgendamentoEstatisticaEmpresa> agendamentosPorEmpresa(LocalDate inicio, LocalDate fim, Long codEmpresa,
                                                              Long codFuncionario);

    Page<Agendamento> filtrar(AgendamentoFilter agendaFilter, Pageable pageable);

    Page<ResumoAgendamento> resumir(AgendamentoFilter agendaFilter, Pageable pageable);

}
