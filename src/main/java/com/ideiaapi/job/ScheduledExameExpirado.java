package com.ideiaapi.job;

import com.ideiaapi.mail.EnvioEmail;
import com.ideiaapi.model.Agendamento;
import com.ideiaapi.model.Contato;
import com.ideiaapi.model.Empresa;
import com.ideiaapi.model.Funcionario;
import com.ideiaapi.repository.AgendamentoRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;

@Component
public class ScheduledExameExpirado {

    private static String emailPoliciaFederal = "psicologos.deleaq.mg@dpf.gov.br";
    private static String emailIdeia = "clinica.ideia@gmail.com";
    private static String emailNilza = "nilzamarquez5@gmail.com";

    @Autowired
    private AgendamentoRepository agendamentoRepository;

    @Autowired
    private EnvioEmail envioEmail;

    /**
     * EXAME EXPIRANDO
     */
    /***
     * A: Segundos (0 – 59).
     * B: Minutos (0 – 59).
     * C: Horas (0 – 23).
     * D: Dia (1 – 31).
     * E: Mês (1 – 12).
     * F: Dia da semana (0 – 6).
     */

    @Scheduled(cron = "0 6 * * * *")
    public void exameExpirando() {

        LocalDate hoje = LocalDate.now();

        List<Agendamento> agendamentosList = this.agendamentoRepository.findAllByDate(hoje.minusDays(335L));
        List<Funcionario> funcionariosList = new ArrayList<>();

        agendamentosList.forEach(agendamento -> funcionariosList.add(agendamento.getFuncionario()));

        funcionariosList.forEach(funcionario -> {

            List<Empresa> empresasList = funcionario.getEmpresas();

            empresasList.forEach(empresa -> {
                List<Contato> contatosList = empresa.getContatos();
                contatosList.forEach(
                        contato -> this.enviarEmailExameExpirando(funcionario, contato.getEmail())
                );
            });

            if (StringUtils.isNotBlank(funcionario.getEmail()))
                this.enviarEmailExameExpirando(funcionario, funcionario.getEmail());
        });
    }

    private void enviarEmailExameExpirando(Funcionario funcionario, String emailEmpresa) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", funcionario.getNome());

        this.envioEmail.enviarEmail(emailIdeia,
                Collections.singletonList(emailEmpresa),
                "Clinica Ideia - Exame expirando",
                "email/exame-expirando", map);
    }
}
