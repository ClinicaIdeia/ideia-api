package com.ideiaapi.repository;

import com.ideiaapi.model.Agendamento;
import com.ideiaapi.repository.agendamento.AgendamentoRepositoryQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, Long>, AgendamentoRepositoryQuery {

    @Query(value = "SELECT * FROM agendamento " +
            "INNER JOIN agenda a ON agendamento.codigo_agenda = a.codigo " +
            "WHERE dia_agenda = ?1", nativeQuery = true)
    List<Agendamento> findAllByAgendaDiaAgenda(LocalDate diaAgenda);

    @Query(value = "SELECT * FROM agendamento " +
            "INNER JOIN agenda a on agendamento.codigo_agenda = a.codigo " +
            "WHERE ?1 = DATE_PART('month', dia_agenda) " +
            "AND ?2 = DATE_PART('year', dia_agenda)", nativeQuery = true)
    List<Agendamento> findAllByAgendaDiaAgendaMonthAndAgendaDiaAgendaYear(Integer month, Integer year);
}
