package com.ideaapi.job;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.ideaapi.base.BaseTest;
import com.ideaapi.mail.EnvioEmail;
import com.ideaapi.model.Agendamento;
import com.ideaapi.model.Contato;
import com.ideaapi.model.Empresa;
import com.ideaapi.model.Funcionario;
import com.ideaapi.repository.AgendamentoRepository;
import com.ideaapi.repository.FuncionarioRepository;

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

    @Before
    public void setUp() throws Exception {
        contato.setEmail("email@empresa");
        contatosList.add(contato);

        empresa.setContatos(contatosList);
        empresasList.add(empresa);

        funcionario.setEmail("email@test");
        funcionario.setNome("Nome");
        funcionario.setEmpresas(empresasList);

        agendamento.setTrabalhoArmado(true);
        agendamento.setFuncionario(funcionario);
        agendamentosList.add(agendamento);
    }

    @Test
    public void envioEmailAniversarioSucesso() {

        funcionario.setDataNascimento(LocalDate.now());
        funcionarioList.add(funcionario);

        when(funcionarioRepository.findAll()).thenReturn(funcionarioList);

        scheduledEmails.aniversario();

        verify(envioEmail, times(1)).enviarEmail(any(), any(), any(), any(), any());
    }

    @Test
    public void envioEmailAniversarioDesnecessario() {

        funcionario.setDataNascimento(LocalDate.now().minusDays(1L));
        funcionarioList.add(funcionario);

        when(funcionarioRepository.findAll()).thenReturn(funcionarioList);

        scheduledEmails.aniversario();

        verify(envioEmail, times(0)).enviarEmail(any(), any(), any(), any(), any());
    }

    @Test
    public void envioExameExpirado() {


        when(agendamentoRepository.findAllByAgenda_DiaAgenda(any())).thenReturn(agendamentosList);

        scheduledEmails.exameExpirando();

        verify(envioEmail, times(2)).enviarEmail(any(), any(), any(), any(), any());
    }

    @Test
    public void envioPoliciaFederal() {

        when(agendamentoRepository.findAllByAgenda_DiaAgenda(any())).thenReturn(agendamentosList);

        scheduledEmails.avisoPoliciaFederal();

        verify(envioEmail, times(1)).enviarEmail(any(), any(), any(), any(), any());
    }

    @Test
    public void emailMensalEmpresas() {

        //TODO: Acabar teste depois de enviar o email

        when(agendamentoRepository.findAllByAgenda_DiaAgenda_MonthAndAgenda_DiaAgenda_Year(any(), any()))
                .thenReturn(agendamentosList);

        scheduledEmails.emailMensalEmpresas();

        verify(envioEmail, times(0)).enviarEmail(any(), any(), any(), any(), any());
    }
}