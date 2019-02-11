package com.ideiaapi.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
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

import com.ideiaapi.base.BaseTest;
import com.ideiaapi.exceptions.BusinessException;
import com.ideiaapi.model.Agenda;
import com.ideiaapi.model.Horario;
import com.ideiaapi.repository.AgendaRepository;

public class AgendaServiceTest extends BaseTest {

    @Mock
    private AgendaRepository agendaRepository;

    @InjectMocks
    private AgendaService agendaService;

    private List<Agenda> agendasList = new ArrayList<>();
    private List<Horario> horariosList = new ArrayList<>();

    private Agenda agenda = new Agenda();
    private Horario horario = new Horario();
    private List<LocalDate> diasCopias = new ArrayList<>();

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
        agenda.setDiasCopia(diasCopias);

        agendasList.add(agenda);
    }

    @Test
    public void listaFuturosAgendamentos() {

        when(agendaRepository.findAllByDiaAgendaAfter(any())).thenReturn(agendasList);

        PageImpl page = agendaService.listaFuturosAgendamentos(false);

        assertNotNull(page);
        assertTrue(1 == page.getTotalElements());
    }

    @Test
    public void copiarAgendaParaUmDia() {

        LocalDate dia1 = LocalDate.now().plusDays(1L);
        diasCopias.add(dia1);

        List<Agenda> response = agendaService.copiaAgenda(agenda);

        assertTrue(response.size() == 1);
        assertTrue("obs".equals(response.get(0).getObservacao()));
        assertTrue(horariosList.equals(response.get(0).getHorarios()));
        assertTrue(response.get(0).getDiasCopia() == null);
    }

    @Test
    public void copiarAgendaParaMaisQueUmDiaOrdenado() {

        LocalDate dia1 = LocalDate.now().plusDays(1L);
        diasCopias.add(dia1);
        LocalDate dia2 = LocalDate.now().plusDays(2L);
        diasCopias.add(dia2);

        agenda.setDiasCopia(diasCopias);

        List<Agenda> response = agendaService.copiaAgenda(agenda);

        assertTrue(response.size() == 2);
        assertTrue("obs".equals(response.get(0).getObservacao()));
        assertTrue(horariosList.equals(response.get(0).getHorarios()));
        assertTrue(response.get(0).getDiasCopia() == null);
        assertTrue("obs".equals(response.get(1).getObservacao()));
        assertTrue(horariosList.equals(response.get(1).getHorarios()));
        assertTrue(response.get(1).getDiasCopia() == null);
    }

    @Test
    public void copiarAgendaParaMaisQueUmDiaDesordenado() {

        LocalDate dia1 = LocalDate.now().plusDays(2L);
        diasCopias.add(dia1);
        LocalDate dia2 = LocalDate.now().plusDays(1L);
        diasCopias.add(dia2);

        agenda.setDiasCopia(diasCopias);

        List<Agenda> response = agendaService.copiaAgenda(agenda);

        assertTrue(response.size() == 2);
        assertTrue("obs".equals(response.get(0).getObservacao()));
        assertTrue(horariosList.equals(response.get(0).getHorarios()));
        assertTrue(response.get(0).getDiasCopia() == null);
        assertTrue("obs".equals(response.get(1).getObservacao()));
        assertTrue(horariosList.equals(response.get(1).getHorarios()));
        assertTrue(response.get(1).getDiasCopia() == null);
    }

    @Test(expected = BusinessException.class)
    public void copiarAgendaError() {

        agenda.setDiasCopia(diasCopias);

        agendaService.copiaAgenda(agenda);
    }
}