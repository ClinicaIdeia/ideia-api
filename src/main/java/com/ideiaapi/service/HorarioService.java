package com.ideiaapi.service;

import com.ideiaapi.model.Agenda;
import com.ideiaapi.model.Horario;
import com.ideiaapi.repository.AgendaRepository;
import com.ideiaapi.repository.HorarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class HorarioService {

    @Autowired
    private HorarioRepository horarioRepository;

    @Autowired
    private AgendaRepository agendaRepository;

    public List<Horario> listaHorarios(Boolean trabalhoArmado) {

        List<Horario> horarios = new ArrayList<>();

        List<Agenda> futurosAgendas;

        if (trabalhoArmado)
            futurosAgendas = agendaRepository.findAllByDiaAgendaAfter(LocalDate.now().plusDays(3L));

        else
            futurosAgendas = agendaRepository.findAllByDiaAgendaAfter(LocalDate.now());

        if (!futurosAgendas.isEmpty())
            futurosAgendas.forEach(agenda ->
                    agenda.getHorarios().forEach(horario ->
                            horarios.add(getHorarioWithHoraExameCompleta(agenda, horario)
                            )
                    )
            );

        return horarios;
    }

    public Horario cadastraHorario(Horario entity) {
        Integer maximoPermitido = entity.getMaximoPermitido();
        Integer restante = maximoPermitido;
        boolean disponivel = true;
        boolean avulso = false;

        if (null != entity.getAvulso() && entity.getAvulso()) {
            maximoPermitido = 1;
            disponivel = false;
            avulso = true;
            restante = 0;
        }
        entity.setAvulso(avulso);
        entity.setRestante(restante);
        entity.setMaximoPermitido(maximoPermitido);
        entity.setDisponivel(disponivel);
        return this.horarioRepository.save(entity);
    }

    public Horario buscaHorario(Long codigo) {
        Horario horario = this.horarioRepository.findOne(codigo);
        if (horario != null) {
            return horario;
        }

        return null;
    }

    public void deletaHorario(Long codigo) {
        this.horarioRepository.delete(codigo);
    }

    public void queimaHorario(Horario horario) {
        int restante = horario.getRestante();
        horario.setRestante(restante - 1);
        this.horarioRepository.save(horario);
    }

    public void devolverHorario(Horario horario) {
        int restante = horario.getRestante();
        horario.setRestante(restante + 1);
        this.horarioRepository.save(horario);
    }

    private Horario getHorarioWithHoraExameCompleta(Agenda agenda, Horario horario) {

        horario.setHoraExame(horario.getHoraExame() + " - " + agenda.getDiaAgenda().getDayOfMonth() + " "
                + agenda.getDiaAgenda().getMonth().toString() + " " + agenda.getDiaAgenda().getYear());

        return horario;
    }

    public void salvaHorarios(List<Horario> horarios) {
        if (!horarios.isEmpty()) {
            horarios.forEach(this::cadastraHorario);
        }
    }

    public List<Horario> copiarHorarios(List<Horario> horarios) {
        List<Horario> novosHorarios = new ArrayList<>();
        horarios.forEach(horario -> {
                Horario novoHorario = new Horario();
                novoHorario.setHoraExame(horario.getHoraExame());
                novoHorario.setAvulso(horario.getAvulso());
                novoHorario.setDisponivel(horario.getDisponivel());
                novoHorario.setMaximoPermitido(horario.getMaximoPermitido());
                novoHorario.setRestante(horario.getMaximoPermitido());
                novoHorario.setAvulso(horario.getAvulso());
                horarioRepository.saveAndFlush(novoHorario);
                novosHorarios.add(novoHorario);
        });

        return novosHorarios;
    }
}
