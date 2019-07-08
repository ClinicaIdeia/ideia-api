package com.ideiaapi.job;

import com.ideiaapi.base.BaseTest;
import com.ideiaapi.mail.EnvioEmail;
import com.ideiaapi.model.*;
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
public class ScheduledMensalEmpresasTest extends BaseTest {

    @Mock
    private AgendamentoRepository agendamentoRepository;

    @Mock
    private EnvioEmail envioEmail;

    @InjectMocks
    private ScheduledMensalEmpresas scheduled;

    private List<Agendamento> agendamentosList = new ArrayList<>();
    private List<Empresa> empresasList = new ArrayList<>();
    private List<Contato> contatosList = new ArrayList<>();

    private Funcionario funcionario = new Funcionario();
    private Agendamento agendamento = new Agendamento();
    private Agenda agenda = new Agenda();
    private Empresa empresa = new Empresa();
    private Contato contato = new Contato();


    @Before
    public void setUp() throws Exception {

        agenda.setDiaAgenda(LocalDate.now().minusMonths(1));

        contato.setEmail("email@empresa");
        contatosList.add(contato);

        empresa.setContatos(contatosList);
        empresasList.add(empresa);

        funcionario.setEmail("email@test");
        funcionario.setNome("Nome");
        funcionario.setSexo("F");
        funcionario.setEmpresas(empresasList);

        agendamento.setTrabalhoArmado(true);
        agendamento.setFuncionario(funcionario);
        agendamentosList.add(agendamento);
        agendamento.setAgenda(agenda);
    }


    @Test
    public void emailMensalEmpresas() {

        when(agendamentoRepository.findAllByMonthAndYear(any(), any())).thenReturn(agendamentosList);

        scheduled.emailMensalEmpresas();

        verify(envioEmail, times(1)).enviarEmail(any(), any(), any(), any(), any());
    }


}
