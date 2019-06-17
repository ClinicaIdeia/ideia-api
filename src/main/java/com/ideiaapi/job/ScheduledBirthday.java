package com.ideiaapi.job;

import com.ideiaapi.mail.EnvioEmail;
import com.ideiaapi.model.Funcionario;
import com.ideiaapi.repository.FuncionarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ScheduledBirthday {

    private static String emailPoliciaFederal = "psicologos.deleaq.mg@dpf.gov.br";
    private static String emailIdeia = "clinica.ideia@gmail.com";
    private static String emailNilza = "nilzamarquez5@gmail.com";


    @Autowired
    private EnvioEmail envioEmail;
    @Autowired
    private FuncionarioRepository funcionarioRepository;

    /**
     * ANIVERSARIO
     */
    /***
     * A: Segundos (0 – 59).
     * B: Minutos (0 – 59).
     * C: Horas (0 – 23).
     * D: Dia (1 – 31).
     * E: Mês (1 – 12).
     * F: Dia da semana (0 – 6).
     */
    @Scheduled(cron = "0 0 6 * * *")
    public void aniversario() {
        LocalDate hoje = LocalDate.now();
        List<Funcionario> funcionarios = this.funcionarioRepository.findByDataNascimentoAndEmailNotNull(hoje.getMonthValue(), hoje.getDayOfMonth());
        funcionarios.forEach(this::enviarEmailAniversario);

    }

    private void enviarEmailAniversario(Funcionario funcionario) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", funcionario.getNome());

        this.envioEmail.enviarEmail(emailIdeia,
                Collections.singletonList(funcionario.getEmail()),
                "Feliz Aniversário", "email/aniversario", map);
    }
}
