package com.ideaapi.job;

import com.ideaapi.base.BaseTest;
import com.ideaapi.mail.EnvioEmail;
import com.ideaapi.model.Funcionario;
import com.ideaapi.repository.FuncionarioRepository;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ScheduledEmailsTest extends BaseTest {

    @Mock
    private FuncionarioRepository funcionarioRepository;

    @Mock
    private EnvioEmail envioEmail;

    @InjectMocks
    private ScheduledEmails scheduledEmails;

    private List<Funcionario> funcionarioList = new ArrayList<>();

    @Test
    public void envioEmailAniversarioSucesso() {

        Funcionario funcionario = new Funcionario();
        funcionario.setEmail("email@test");
        funcionario.setNome("Nome");
        funcionario.setDataNascimento(LocalDate.now());

        funcionarioList.add(funcionario);

        when(funcionarioRepository.findAll()).thenReturn(funcionarioList);

        scheduledEmails.execution();

        verify(envioEmail, times(1)).enviarEmail(any(), any(), any(), any(), any());
    }

    @Test
    public void envioEmailAniversarioDesnecessario() {

        Funcionario funcionario = new Funcionario();
        funcionario.setEmail("email@test");
        funcionario.setNome("Nome");
        funcionario.setDataNascimento(LocalDate.now().minusDays(1L));

        funcionarioList.add(funcionario);

        when(funcionarioRepository.findAll()).thenReturn(funcionarioList);

        scheduledEmails.execution();

        verify(envioEmail, times(0)).enviarEmail(any(), any(), any(), any(), any());
    }
}