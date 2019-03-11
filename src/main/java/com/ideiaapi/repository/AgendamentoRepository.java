package com.ideiaapi.repository;

<<<<<<< HEAD
import com.ideiaapi.model.Agendamento;
import com.ideiaapi.repository.agendamento.AgendamentoRepositoryQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
=======
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ideiaapi.model.Agendamento;
import com.ideiaapi.repository.agendamento.AgendamentoRepositoryQuery;
>>>>>>> 9f0c188a5ed62007b52e8a078edc8aa554a9eb8a

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, Long>, AgendamentoRepositoryQuery {

    @Query(value = "SELECT * FROM agendamento " +
            "INNER JOIN agenda a ON agendamento.codigo_agenda = a.codigo " +
            "WHERE dia_agenda = ?1", nativeQuery = true)
<<<<<<< HEAD
    List<Agendamento> findAllByAgendaDiaAgenda(LocalDate diaAgenda);

    @Query(value = "SELECT * FROM agendamento " +
            "INNER JOIN agenda a on agendamento.codigo_agenda = a.codigo " +
            "WHERE ?1 = DATE_PART('month', dia_agenda) " +
            "AND ?2 = DATE_PART('year', dia_agenda)", nativeQuery = true)
    List<Agendamento> findAllByAgendaDiaAgendaMonthAndAgendaDiaAgendaYear(Integer month, Integer year);
=======
    List<Agendamento> findAllByDate(LocalDate diaAgenda);

    @Query(value = "SELECT * FROM agendamento " +
            "INNER JOIN agenda a on agendamento.codigo_agenda = a.codigo " +
            "WHERE ?1 = DATE_PART('month', dia_agenda) " +
            "AND ?2 = DATE_PART('year', dia_agenda)", nativeQuery = true)
    List<Agendamento> findAllByMonthAndYear(Integer month, Integer year);

    @Query(value = "SELECT * FROM agendamento WHERE emitiu_laudo = false", nativeQuery = true)
    List<Agendamento> findAllByAindaNaoEmitiuLaudo();

    @Query(value = "select * from agendamento age inner join funcionario_empresa fe on fe.codigo_funcionario = age.codigo_funcionario\n" +
            "        inner join agenda agd on agd.codigo = age.codigo_agenda\n" +
            "where fe.codigo_empresa = :codigo and agd.dia_agenda between :inicio and :fim", nativeQuery = true)
    List<Agendamento> findAllByAgendamentoRelatorioPorEmpresa(
            @Param(value = "inicio") LocalDate inicio,
            @Param(value = "fim") LocalDate fim,
            @Param(value = "codigo") Long codigo);

    @Query(value = "select * from agendamento age inner join funcionario_empresa fe on fe.codigo_funcionario = age.codigo_funcionario\n" +
            "        inner join agenda agd on agd.codigo = age.codigo_agenda\n" +
            "where agd.dia_agenda between :inicio and :fim", nativeQuery = true)
    List<Agendamento> findAllByAgendamentoRelatorioPorEmpresaAdmin(
            @Param(value = "inicio") LocalDate inicio,
            @Param(value = "fim") LocalDate fim);
>>>>>>> 9f0c188a5ed62007b52e8a078edc8aa554a9eb8a
}
