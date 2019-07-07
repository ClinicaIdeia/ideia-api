package com.ideiaapi.job;

import com.ideiaapi.mail.EnvioEmail;
import com.ideiaapi.model.Funcionario;
import com.ideiaapi.repository.FuncionarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Component
public class ScheduledNatal {

    private static final String EMAIL_IDEIA = "clinica.ideia@gmail.com";

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private EnvioEmail envioEmail;

    /**
     * NATAL
     */
    /***
     * A: Segundos (0 – 59).
     * B: Minutos (0 – 59).
     * C: Horas (0 – 23).
     * D: Dia (1 – 31).
     * E: Mês (1 – 12).
     * F: Dia da semana (0 – 6).
     */
    @Scheduled(cron = "0 0 10 25 12 *")
    public void natal() {
        this.funcionarioRepository.findAllByEmailIsNotNull()
                .forEach(this::enviarEmailNatal);
    }

    private void enviarEmailNatal(Funcionario funcionario) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", funcionario.getNome().toUpperCase());

        this.envioEmail.enviarEmail(EMAIL_IDEIA,
                Collections.singletonList(funcionario.getEmail()),
                "Feliz Natal",
                "email/natal", map);
    }
}
