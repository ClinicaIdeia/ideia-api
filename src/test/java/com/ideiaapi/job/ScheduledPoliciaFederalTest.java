package com.ideiaapi.job;

import com.ideiaapi.base.BaseTest;
import com.ideiaapi.mail.EnvioEmail;
import com.ideiaapi.model.Agenda;
import com.ideiaapi.model.Agendamento;
import com.ideiaapi.model.Funcionario;
import com.ideiaapi.repository.AgendamentoRepository;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@Ignore
public class ScheduledPoliciaFederalTest extends BaseTest {


    @Mock
    private AgendamentoRepository agendamentoRepository;

    @Mock
    private EnvioEmail envioEmail;

    @InjectMocks
    private ScheduledPoliciaFederal scheduled;

    private List<Agendamento> agendamentosList = new ArrayList<>();

    private Funcionario funcionario = new Funcionario();
    private Agendamento agendamento = new Agendamento();
    private Agenda agenda = new Agenda();

    @Before
    public void setUp() throws Exception {

        agenda.setDiaAgenda(LocalDate.now().minusMonths(1));

        agendamento.setTrabalhoArmado(true);
        agendamento.setFuncionario(funcionario);
        agendamentosList.add(agendamento);
        agendamento.setAgenda(agenda);

    }

    @Ignore
    @Test
    public void envioPoliciaFederal() {

        when(agendamentoRepository.findAllByDate(any())).thenReturn(agendamentosList);

        scheduled.avisoPoliciaFederal();

        verify(envioEmail, times(1)).enviarEmail(any(), any(), any(), any(), any());
    }
}
