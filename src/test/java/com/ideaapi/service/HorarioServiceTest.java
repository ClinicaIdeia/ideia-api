package com.ideaapi.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.ideaapi.base.BaseTest;
import com.ideaapi.model.Agenda;
import com.ideaapi.model.Horario;
import com.ideaapi.repository.AgendaRepository;
import com.ideaapi.repository.HorarioRepository;

public class HorarioServiceTest extends BaseTest {

    @Mock
    private HorarioRepository horarioRepository;

    @Mock
    private AgendaRepository agendaRepository;

    @InjectMocks
    private HorarioService horarioService;

    private List<Agenda> agendaList = new ArrayList<>();

    @Before
    public void setUp() throws Exception {

        Horario horario1 = new Horario();
        horario1.setHoraExame("8:00");

        Horario horario2 = new Horario();
        horario2.setHoraExame("9:00");

        Horario horario3 = new Horario();
        horario3.setHoraExame("10:00");

        List<Horario> horarioList1 = new ArrayList<>();
        horarioList1.add(horario1);
        Agenda agenda1 = new Agenda();
        agenda1.setHorarios(horarioList1);
        agenda1.setDiaAgenda(LocalDate.now().plusDays(4));

        List<Horario> horarioList2 = new ArrayList<>();
        horarioList2.add(horario1);
        horarioList2.add(horario2);
        Agenda agenda2 = new Agenda();
        agenda2.setHorarios(horarioList2);
        agenda2.setDiaAgenda(LocalDate.now().plusDays(5));

        List<Horario> horarioList3 = new ArrayList<>();
        horarioList3.add(horario1);
        horarioList3.add(horario2);
        horarioList3.add(horario3);
        Agenda agenda3 = new Agenda();
        agenda3.setHorarios(horarioList3);
        agenda3.setDiaAgenda(LocalDate.now().plusDays(6));

        agendaList.add(agenda1);
        agendaList.add(agenda2);
        agendaList.add(agenda3);
    }

    @Test
    public void listaHorariosDos3ProximosDiasSucessoArmaDeFogo() {

        when(agendaRepository.findAllByDiaAgendaAfter(LocalDate.now().plusDays(3L))).thenReturn(agendaList);

        List<Horario> response = horarioService.listaHorarios(true);

        assertEquals(6, response.size());
    }

    @Test
    public void listaHorariosDos3ProximosDiasSucessoSemArmaDeFogo() {

        when(agendaRepository.findAllByDiaAgendaAfter(LocalDate.now())).thenReturn(agendaList);

        List<Horario> response = horarioService.listaHorarios(false);

        assertEquals(6, response.size());
    }
}