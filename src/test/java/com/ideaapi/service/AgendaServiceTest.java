package com.ideaapi.service;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.PageImpl;

import com.ideaapi.base.BaseTest;
import com.ideaapi.model.Agenda;
import com.ideaapi.model.Horario;
import com.ideaapi.repository.AgendaRepository;

public class AgendaServiceTest extends BaseTest {

    @Mock
    private AgendaRepository agendaRepository;

    @InjectMocks
    private AgendaService agendaService;

    private List<Agenda> agendasList = new ArrayList<>();
    private List<Horario> horariosList = new ArrayList<>();

    private Agenda agenda = new Agenda();
    private Horario horario = new Horario();

    @Before
    public void setUp() throws Exception {

        horario.setHoraExame("8:00");
        horario.setCodigo(0L);
        horario.setDisponivel(true);
        horario.setRestante(2);
        horario.setAvulso(true);
        horario.setMaximoPermitido(15);

        horariosList.add(horario);

        agenda.setDiaAgenda(LocalDate.now());
        agenda.setCodigo(1L);
        agenda.setObservacao("obs");
        agenda.setHorarios(horariosList);

        agendasList.add(agenda);
    }

    @Test
    public void listaFuturosAgendamentos() {

        when(agendaRepository.findAllByDiaAgendaAfter(any())).thenReturn(agendasList);

        PageImpl page = agendaService.listaFuturosAgendamentos();

        assertNotNull(page);
        assertTrue(1 == page.getTotalElements());
    }
}