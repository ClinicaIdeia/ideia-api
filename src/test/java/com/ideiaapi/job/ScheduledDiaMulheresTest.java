package com.ideiaapi.job;

import com.ideiaapi.base.BaseTest;
import com.ideiaapi.mail.EnvioEmail;
import com.ideiaapi.model.Contato;
import com.ideiaapi.model.Empresa;
import com.ideiaapi.model.Funcionario;
import com.ideiaapi.repository.FuncionarioRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class ScheduledDiaMulheresTest extends BaseTest {
    @Mock
    private FuncionarioRepository funcionarioRepository;

    @Mock
    private EnvioEmail envioEmail;

    @InjectMocks
    private ScheduledDiaMulheres scheduled;

    private List<Funcionario> funcionarioList = new ArrayList<>();
    private List<Empresa> empresasList = new ArrayList<>();
    private List<Contato> contatosList = new ArrayList<>();

    private Funcionario funcionario = new Funcionario();
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
        funcionario.setSexo("F");
        funcionario.setEmpresas(empresasList);
    }

    @Test
    public void envioEmailDiaMulheres() {

        funcionarioList.add(funcionario);

        when(funcionarioRepository.findAll()).thenReturn(funcionarioList);

        scheduled.diaMulheres();

        verify(envioEmail, times(1)).enviarEmail(any(), any(), any(), any(), any());
    }
}
