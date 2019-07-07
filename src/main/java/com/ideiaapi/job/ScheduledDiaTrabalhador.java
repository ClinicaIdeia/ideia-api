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
public class ScheduledDiaTrabalhador {

    private static final String EMAIL_IDEIA = "clinica.ideia@gmail.com";

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private EnvioEmail envioEmail;

    /**
     * DIA DO TRABALHADOR
     */
    /***
     * A: Segundos (0 – 59).
     * B: Minutos (0 – 59).
     * C: Horas (0 – 23).
     * D: Dia (1 – 31).
     * E: Mês (1 – 12).
     * F: Dia da semana (0 – 6).
     */
    @Scheduled(cron = "0 0 6 1 5 *")
    public void diaTrabalhador() {

        this.funcionarioRepository.findAllByEmailIsNotNull()
                .forEach(this::enviarEmailDiaTrabalhador);


    }

    private void enviarEmailDiaTrabalhador(Funcionario funcionario) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", funcionario.getNome());

        this.envioEmail.enviarEmail(EMAIL_IDEIA,
                Collections.singletonList(funcionario.getEmail()),
                "Feliz Dia do Trabalhador",
                "email/dia-trabalhador", map);
    }
}
