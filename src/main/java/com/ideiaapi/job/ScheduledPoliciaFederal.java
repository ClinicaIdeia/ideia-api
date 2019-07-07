package com.ideiaapi.job;

import com.ideiaapi.mail.EnvioEmail;
import com.ideiaapi.model.Agendamento;
import com.ideiaapi.model.Funcionario;
import com.ideiaapi.repository.AgendamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;

@Component
public class ScheduledPoliciaFederal {

    private static String emailPoliciaFederal = "psicologos.deleaq.mg@dpf.gov.br";
    private static String emailIdeia = "clinica.ideia@gmail.com";
    private static String emailNilza = "nilzamarquez5@gmail.com";

    @Autowired
    private AgendamentoRepository agendamentoRepository;

    @Autowired
    private EnvioEmail envioEmail;

    /**
     * POLICIA FEDERAL
     */
    /***
     * A: Segundos (0 – 59).
     * B: Minutos (0 – 59).
     * C: Horas (0 – 23).
     * D: Dia (1 – 31).
     * E: Mês (1 – 12).
     * F: Dia da semana (0 – 6).
     */

//    @Scheduled(cron = "0 16 * * * *")
    public void avisoPoliciaFederal() {

        LocalDate hoje = LocalDate.now();

        List<Agendamento> agendamentosList = this.agendamentoRepository.findAllByDate(hoje.plusDays(3L));
        List<Funcionario> funcionariosList = new ArrayList<>();

        // Alterar para mandar somenteo como os moitvos Porte de arma, Registrop de Arma, certificadpo de registor de marma e renovação de certificado de registro
        agendamentosList.stream()
                .filter(Agendamento::getTrabalhoArmado)
                .forEach(agendamento -> funcionariosList.add(agendamento.getFuncionario()));

        this.enviarEmailPoliciaFederal(funcionariosList);
    }

    private void enviarEmailPoliciaFederal(List<Funcionario> funcionariosList) {
        Map<String, Object> map = new HashMap<>();
        List<String> nomesFuncionarios = new ArrayList<>();

        funcionariosList.forEach(funcionario -> nomesFuncionarios.add(funcionario.getNome()));
        map.put("names", nomesFuncionarios);

        this.envioEmail.enviarEmail(emailIdeia,
//                Collections.singletonList(emailPoliciaFederal),
//                TODO : Colocar de volta quando enviar diretamente para Policia Federal
                Collections.singletonList(emailNilza),
                "Clinica Ideia - Agendamentos de exames psicologicos para trabalhos armados",
                "email/policia-federal", map);
    }


    /*
    as 18
    texto:Olá. Segue agenda do dia X

     */
}
