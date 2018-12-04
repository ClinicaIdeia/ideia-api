package com.ideaapi.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ideaapi.model.Agendamento;
import com.ideaapi.repository.AgendamentoRepository;
import com.ideaapi.repository.filter.AgendamentoFilter;
import com.ideaapi.repository.projection.ResumoAgendamento;

@Service
public class AgendamentoService {

    @Autowired
    private AgendamentoRepository agendamentoRepository;

    @Autowired
    private HorarioService horarioService;

    public Page<Agendamento> listaTodasAgendamentos(AgendamentoFilter filter, Pageable pageable) {
        return this.agendamentoRepository.filtrar(filter, pageable);
    }

    public Page<ResumoAgendamento> resumo(AgendamentoFilter filter, Pageable pageable) {
        return this.agendamentoRepository.resumir(filter, pageable);
    }

    public Agendamento cadastraAgendamento(Agendamento entity) {
        this.horarioService.queimaHorario(entity.getCodHorario());
        return this.agendamentoRepository.save(entity);
    }

    public Agendamento buscaAgendamento(Long codigo) {
        Agendamento agendamento = this.agendamentoRepository.findOne(codigo);

        if (agendamento == null) {
            throw new EmptyResultDataAccessException(1);
        }

        return agendamento;
    }

    public void deletaAgendamento(Long codigo) {
        this.agendamentoRepository.delete(codigo);
    }

    public ResponseEntity<Agendamento> atualizaAgendamento(Long codigo, Agendamento agendamento) {
        Agendamento agendamentoSalvo = this.buscaAgendamento(codigo);
        BeanUtils.copyProperties(agendamento, agendamentoSalvo, "codigo");

        this.agendamentoRepository.save(agendamentoSalvo);
        return ResponseEntity.ok(agendamentoSalvo);
    }

}
