package com.ideiaapi.job;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.ideiaapi.base.BaseTest;
import com.ideiaapi.mail.EnvioEmail;
import com.ideiaapi.model.Agenda;
import com.ideiaapi.model.Agendamento;
import com.ideiaapi.model.Contato;
import com.ideiaapi.model.Empresa;
import com.ideiaapi.model.Funcionario;
import com.ideiaapi.repository.AgendamentoRepository;
import com.ideiaapi.repository.FuncionarioRepository;

public class ScheduledEmailsTest extends BaseTest {

    @Mock
    private FuncionarioRepository funcionarioRepository;

    @Mock
    private AgendamentoRepository agendamentoRepository
            ;

    @Mock
    private EnvioEmail envioEmail;

    @InjectMocks
    private ScheduledEmails scheduledEmails;

    private List<Funcionario> funcionarioList = new ArrayList<>();
    private List<Agendamento> agendamentosList = new ArrayList<>();
    private List<Empresa> empresasList = new ArrayList<>();
    private List<Contato> contatosList = new ArrayList<>();

    private Funcionario funcionario = new Funcionario();
    private Agendamento agendamento = new Agendamento();
    private Empresa empresa = new Empresa();
    private Contato contato = new Contato();
    private Agenda agenda = new Agenda();

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

    @Ignore
    @Test
    public void envioEmailAniversarioSucesso() {

        funcionario.setDataNascimento(LocalDate.now());
        funcionarioList.add(funcionario);

        when(funcionarioRepository.findAll()).thenReturn(funcionarioList);

//        scheduledEmails.aniversario();

        verify(envioEmail, times(1)).enviarEmail(any(), any(), any(), any(), any());
    }

    @Ignore
    @Test
    public void envioEmailAniversarioDesnecessario() {

        funcionario.setDataNascimento(LocalDate.now().minusDays(1L));
        funcionarioList.add(funcionario);

        when(funcionarioRepository.findAll()).thenReturn(funcionarioList);

//        scheduledEmails.aniversario();

        verify(envioEmail, times(0)).enviarEmail(any(), any(), any(), any(), any());
    }

    @Test
    public void envioExameExpirado() {


        when(agendamentoRepository.findAllByDate(any())).thenReturn(agendamentosList);

        scheduledEmails.exameExpirando();

        verify(envioEmail, times(2)).enviarEmail(any(), any(), any(), any(), any());
    }

    @Test
    public void envioPoliciaFederal() {

        when(agendamentoRepository.findAllByDate(any())).thenReturn(agendamentosList);

        scheduledEmails.avisoPoliciaFederal();

        verify(envioEmail, times(1)).enviarEmail(any(), any(), any(), any(), any());
    }

    @Test
    public void emailMensalEmpresas() {
        when(agendamentoRepository.findAllByMonthAndYear(any(), any())).thenReturn(agendamentosList);

        scheduledEmails.emailMensalEmpresas();

        verify(envioEmail, times(1)).enviarEmail(any(), any(), any(), any(), any());
    }

    @Test
    public void envioEmailAnoNovo() {

        funcionarioList.add(funcionario);

        when(funcionarioRepository.findAll()).thenReturn(funcionarioList);

        scheduledEmails.anoNovo();

        verify(envioEmail, times(1)).enviarEmail(any(), any(), any(), any(), any());
    }

    @Test
    public void envioEmailNatal() {

        funcionarioList.add(funcionario);

        when(funcionarioRepository.findAll()).thenReturn(funcionarioList);

        scheduledEmails.natal();

        verify(envioEmail, times(1)).enviarEmail(any(), any(), any(), any(), any());
    }

    @Test
    public void envioEmailDiaAmigo() {

        funcionarioList.add(funcionario);

        when(funcionarioRepository.findAll()).thenReturn(funcionarioList);

        scheduledEmails.diaAmigo();

        verify(envioEmail, times(1)).enviarEmail(any(), any(), any(), any(), any());
    }

    @Test
    public void envioEmailDiaTrabalhador() {

        funcionarioList.add(funcionario);

        when(funcionarioRepository.findAll()).thenReturn(funcionarioList);

        scheduledEmails.diaTrabalhador();

        verify(envioEmail, times(1)).enviarEmail(any(), any(), any(), any(), any());
    }

    @Test
    public void envioEmailDiaMulheres() {

        funcionarioList.add(funcionario);

        when(funcionarioRepository.findAll()).thenReturn(funcionarioList);

        scheduledEmails.diaMulheres();

        verify(envioEmail, times(1)).enviarEmail(any(), any(), any(), any(), any());
    }
}