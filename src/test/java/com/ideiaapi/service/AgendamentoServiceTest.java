package com.ideiaapi.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.dao.IncorrectResultSizeDataAccessException;

import com.ideiaapi.base.BaseTest;
import com.ideiaapi.model.Agendamento;
import com.ideiaapi.model.Horario;
import com.ideiaapi.repository.AgendamentoRepository;

public class AgendamentoServiceTest extends BaseTest {

    @Mock
    private AgendamentoRepository agendamentoRepository;

    @Mock
    private HorarioService horarioService;

    @Mock
    private AgendaService agendaService;

    @InjectMocks
    private AgendamentoService agendamentoService;

    private Horario horario = new Horario();
    private Agendamento agendamento = new Agendamento();

    @Before
    public void setUp() throws Exception {

        horario.setHoraExame("8:00");
        horario.setCodigo(0L);
        horario.setDisponivel(true);
        horario.setRestante(2);
        horario.setAvulso(true);
        horario.setMaximoPermitido(4);

        agendamento.setCodHorario(0L);
        agendamento.setAvulso(false);
    }

    @Test
    public void deletaAgendamentoNaoAvulso() {

        when(agendamentoRepository.findOne(1L)).thenReturn(agendamento);
        when(horarioService.buscaHorario(agendamento.getCodHorario())).thenReturn(horario);

        agendamentoService.deletaAgendamento(1L);

        verify(horarioService, times(1)).devolverHorario(horario);
        verify(agendamentoRepository, times(1)).delete(1L);
    }

    @Test
    public void deletaAgendamentoAvulso() {

        agendamento.setAvulso(true);

        when(agendamentoRepository.findOne(1L)).thenReturn(agendamento);

        agendamentoService.deletaAgendamento(1L);

        verify(horarioService, times(0)).devolverHorario(horario);
        verify(agendamentoRepository, times(1)).delete(1L);
    }

    @Test(expected = IncorrectResultSizeDataAccessException.class)
    public void deletaAgendamentoError() {

        agendamentoService.deletaAgendamento(1L);
    }
}