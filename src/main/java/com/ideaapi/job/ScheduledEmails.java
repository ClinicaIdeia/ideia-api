package com.ideaapi.job;

import com.ideaapi.mail.EnvioEmail;
import com.ideaapi.model.Funcionario;
import com.ideaapi.repository.FuncionarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ScheduledEmails {

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private EnvioEmail envioEmail;

    @Scheduled(cron = "0 6 * * * *")
    public void execution() {

        List<Funcionario> funcionarioList = this.funcionarioRepository.findAll();

        LocalDate hoje = LocalDate.now();


        funcionarioList.stream()
                .filter(funcionario -> this.diaDeAniversarioHoje(funcionario, hoje))
                .forEach(funcionario -> {

                            Map<String, Object> map = new HashMap<>();
                            map.put("name", funcionario.getNome());

                            this.envioEmail.enviarEmail("openlinkti@gmail.com", Collections.singletonList(funcionario.getEmail()),
                                    "O Sistema Ideia deseja um feliz aniversário para você", "email/aniversario", map);
                        }
                );

    }

    private Boolean diaDeAniversarioHoje(Funcionario funcionario, LocalDate hoje) {

        LocalDate dataNascimento = funcionario.getDataNascimento();

        return (dataNascimento.getDayOfMonth() == hoje.getDayOfMonth()) && (dataNascimento.getMonth() == hoje.getMonth());

    }

}
