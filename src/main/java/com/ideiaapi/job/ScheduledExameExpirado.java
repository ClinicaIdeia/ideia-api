package com.ideiaapi.job;

import com.ideiaapi.mail.EnvioEmail;
import com.ideiaapi.model.Funcionario;
import com.ideiaapi.repository.AgendamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Component
public class ScheduledExameExpirado {

    private static final String EMAIL_IDEIA = "clinica.ideia@gmail.com";
    private static final String EMAIL_NILZA = "nilzamarquez5@gmail.com";
    private static final String AUDITOR = "alvesdesouzaalex@gmail.com";

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
    @Scheduled(cron = "0 0 13 * * *")
    public void exameExpirando() {

        this.agendamentoRepository.findAllByDate(LocalDate.now().minusDays(335L))
                .forEach(agendamento -> {

                    final Funcionario funcionario = agendamento.getFuncionario();
                    String nome = funcionario.getNome().concat(" - ").concat(funcionario.getNumeroCadastro().toString());
                    this.enviarEmailExameExpirando(nome);

                });

    }

    private void enviarEmailExameExpirando(String nome) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", nome);

        this.envioEmail.enviarEmail(EMAIL_IDEIA,
                Arrays.asList(EMAIL_NILZA, EMAIL_IDEIA, AUDITOR),
                "Clinica Ideia - Exame expirando",
                "email/exame-expirando", map);
    }
}
