package com.ideiaapi.job;

import com.ideiaapi.enums.OrientacaoSexual;
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
public class ScheduledDiaMulheres {

    private static String emailIdeia = "clinica.ideia@gmail.com";

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private EnvioEmail envioEmail;

    /**
     * DIA DAS MULHERES
     */
    /***
     * A: Segundos (0 – 59).
     * B: Minutos (0 – 59).
     * C: Horas (0 – 23).
     * D: Dia (1 – 31).
     * E: Mês (1 – 12).
     * F: Dia da semana (0 – 6).
     */
    @Scheduled(cron = "0 0 6 8 3 *")
    public void diaMulheres() {

        this.funcionarioRepository.findAllByEmailIsNotNullAndSexo(OrientacaoSexual.FEMININO.name())
                .forEach(this::enviarEmailDiaMulheres);

    }

    private void enviarEmailDiaMulheres(Funcionario funcionario) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", funcionario.getNome());

        this.envioEmail.enviarEmail(emailIdeia,
                Collections.singletonList(funcionario.getEmail()),
                "Feliz Dia da Mulher",
                "email/dia-mulheres", map);
    }
}
