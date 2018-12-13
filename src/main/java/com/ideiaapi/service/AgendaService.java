package com.ideiaapi.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ideiaapi.model.Agenda;
import com.ideiaapi.repository.AgendaRepository;
import com.ideiaapi.repository.filter.AgendaFilter;
import com.ideiaapi.repository.projection.ResumoAgendamento;

@Service
public class AgendaService {

    @Autowired
    private AgendaRepository agendaRepository;

    @Autowired
    private HorarioService horarioService;

    public Page<Agenda> listaAgendamentos(AgendaFilter filter, Pageable pageable) {
        return this.agendaRepository.filtrar(filter, pageable);
    }

    public PageImpl<List<Agenda>> listaFuturosAgendamentos() {
        List<Agenda> agendasList = agendaRepository.findAllByDiaAgendaAfter(LocalDate.now());
        return new PageImpl(agendasList);
    }

    public Page<ResumoAgendamento> resumo(AgendaFilter filter, Pageable pageable) {
        return this.agendaRepository.resumir(filter, pageable);
    }

    public Agenda cadastraAgenda(Agenda entity) {

        if (!entity.getHorarios().isEmpty()) {
            entity.getHorarios().forEach(this.horarioService::cadastraHorario);
        }
        return this.agendaRepository.save(entity);
    }

    public Agenda buscaAgendamento(Long codigo) {
        Agenda agenda = this.agendaRepository.findOne(codigo);

        if (agenda == null) {
            throw new EmptyResultDataAccessException(1);
        }

        return agenda;
    }

    public void deletaAgendamento(Long codigo) {
        this.agendaRepository.delete(codigo);
    }

    public ResponseEntity<Agenda> atualizaAgendamento(Long codigo, Agenda agenda) {
        Agenda agendaSalva = this.buscaAgendamento(codigo);
        BeanUtils.copyProperties(agenda, agendaSalva, "codigo");

        this.agendaRepository.save(agendaSalva);
        return ResponseEntity.ok(agendaSalva);
    }

}