package com.ideiaapi.service;

import java.io.InputStream;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import com.ideiaapi.model.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ideiaapi.dto.AgendamentoEstatisticaEmpresa;
import com.ideiaapi.exceptions.BusinessException;
import com.ideiaapi.repository.AgendamentoRepository;
import com.ideiaapi.repository.filter.AgendamentoFilter;
import com.ideiaapi.repository.projection.ResumoAgendamento;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
public class AgendamentoService {

    @Autowired
    private AgendamentoRepository agendamentoRepository;

    @Autowired
    private HorarioService horarioService;

    @Autowired
    private AgendaService agendaService;

    public byte[] relatorioPorEmpresa(LocalDate inicio, LocalDate fim, Long codEmpresa,
            Long codFuncionario) throws Exception { //NOSONAR

        List<AgendamentoEstatisticaEmpresa> dados = this.agendamentoRepository.agendamentosPorEmpresa(inicio, fim,
                codEmpresa, codFuncionario);

        Map<String, Object> parametros = new HashMap<>();
        parametros.put("DT_INICIO", Date.valueOf(inicio));
        parametros.put("DT_FIM", Date.valueOf(fim));

        InputStream inputStream = this.getClass().getResourceAsStream("/relatorios/agendamentos.jasper");

        JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream, parametros,
                new JRBeanCollectionDataSource(dados));

        return JasperExportManager.exportReportToPdf(jasperPrint);
    }

    public Page<Agendamento> listaAgendamentos(AgendamentoFilter filter, Pageable pageable) {
        Page<Agendamento> agendamentos = this.agendamentoRepository.filtrar(filter, pageable);
        agendamentos.iterator().forEachRemaining(agendamento -> {
            final Funcionario funcionario = agendamento.getFuncionario();
            String nomeFuncNum = funcionario.getNome() + " - " + funcionario.getNumeroCadastro();
            agendamento.getFuncionario().setNomeFuncNum(nomeFuncNum);
        });
        return agendamentos;
    }

    public Page<ResumoAgendamento> resumo(AgendamentoFilter filter, Pageable pageable) {
        return this.agendamentoRepository.resumir(filter, pageable);
    }

    @Transactional
    public Agendamento cadastraAgendamento(Agendamento entity) {

        if (null == entity.getLaudoGerado()) {
            entity.setLaudoGerado(false);
        }

        Horario horario = null;

        if (!entity.getAvulso()) {

            horario = horarioService.buscaHorario(entity.getCodHorario());
            this.horarioService.queimaHorario(horario);

        } else {

            Agenda agenda = entity.getAgenda();
            this.agendaService.cadastraAgenda(agenda);
            final Optional<Horario> horaAgenda = agenda.getHorarios().stream().filter(Horario::getAvulso).findFirst();
            if (horaAgenda.isPresent()) {
                horario = horaAgenda.get();
            }

        }

        if (null != horario) {
            LocalTime parse = LocalTime.parse(horario.getHoraExame());
            entity.setHoraExame(parse);
        }

        this.insereEmpresaSeNaoExistir(entity);

        return this.agendamentoRepository.save(entity);
    }

    private void insereEmpresaSeNaoExistir(Agendamento entity) {
        if (null == entity.getEmpresa() &&
                (null != entity.getFuncionario().getEmpresas() && !entity.getFuncionario().getEmpresas().isEmpty())
                || entity.getEmpresa().getCodigo() == null) {

            Optional<Empresa> empresa = entity.getFuncionario().getEmpresas().stream().findFirst();
            if (!empresa.isPresent()) {
                throw new BusinessException("ERROR-AGE-ADM");
            }
            entity.setEmpresa(empresa.get());
        }
    }

    public Agendamento buscaAgendamento(Long codigo) {
        Agendamento agendamento = this.agendamentoRepository.findOne(codigo);

        if (agendamento == null) {
            throw new EmptyResultDataAccessException(1);
        }

        return agendamento;
    }

    public void deletaAgendamento(Long codigo) {

        Agendamento agendamento = this.agendamentoRepository.findOne(codigo);

        if (agendamento == null)
            throw new EmptyResultDataAccessException(1);

        if (!agendamento.getAvulso() && (null != agendamento.getAgenda().getHorarios() &&
                !agendamento.getAgenda().getHorarios().isEmpty())) {

            agendamento.getAgenda().getHorarios().forEach(horario -> {
                LocalTime parse = LocalTime.parse(horario.getHoraExame());
                if (parse.equals(agendamento.getHoraExame())) {
                    this.horarioService.devolverHorario(horario);
                    return;
                }
            });

        }

        this.agendamentoRepository.delete(codigo);
    }

    public ResponseEntity<Agendamento> atualizaAgendamento(Long codigo, Agendamento agendamento) {
        Agendamento agendamentoSalvo = this.buscaAgendamento(codigo);
        BeanUtils.copyProperties(agendamento, agendamentoSalvo, "codigo");

        this.agendamentoRepository.save(agendamentoSalvo);
        return ResponseEntity.ok(agendamentoSalvo);
    }

    public void removeLaudoGeradoAgendamento(Agendamento agendamento) {

        Agendamento agendamentoSalvo = this.buscaAgendamento(agendamento.getCodigo());
        agendamentoSalvo.setLaudoGerado(false);
        BeanUtils.copyProperties(agendamento, agendamentoSalvo, "codigo");

        this.agendamentoRepository.save(agendamentoSalvo);
    }

    public List<Agendamento> agendamentosParaLaudo() {
        return this.agendamentoRepository.findAllByAindaNaoEmitiuLaudo();
    }

    public void marcarLaudoGerado(Laudo laudo) {
        Long codAgendamento = laudo.getAgendamento().getCodigo();
        Agendamento agendamento = this.buscaAgendamento(codAgendamento);
        agendamento.setLaudoGerado(true);
        this.atualizaAgendamento(codAgendamento, agendamento);
    }
}
