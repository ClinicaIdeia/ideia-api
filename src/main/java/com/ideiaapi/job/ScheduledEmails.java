package com.ideiaapi.job;

import com.ideiaapi.mail.EnvioEmail;
import com.ideiaapi.model.Agendamento;
import com.ideiaapi.model.Contato;
import com.ideiaapi.model.Empresa;
import com.ideiaapi.model.Funcionario;
import com.ideiaapi.repository.AgendamentoRepository;
import com.ideiaapi.repository.FuncionarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;

@Component
public class ScheduledEmails {

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private AgendamentoRepository agendamentoRepository;

    @Autowired
    private EnvioEmail envioEmail;

    private static String emailPoliciaFederal = "psicologos.deleaq.mg@dpf.gov.br";
    private static String emailIdeia = "clinica.ideia@gmail.com";

    @Scheduled(cron = "0 6 * * * *")
    public void aniversario() {

        List<Funcionario> funcionarioList = this.funcionarioRepository.findAll();

        LocalDate hoje = LocalDate.now();

        funcionarioList.stream()
                .filter(funcionario -> this.diaDeAniversarioHoje(funcionario, hoje))
                .forEach(this::enviarEmailAniversario);
    }

    @Scheduled(cron = "0 6 * * * *")
    public void exameExpirando() {

        LocalDate hoje = LocalDate.now();

        List<Agendamento> agendamentosList = this.agendamentoRepository.findAllByAgendaDiaAgenda(hoje.minusDays(335L));
        List<Funcionario> funcionariosList = new ArrayList<>();

        agendamentosList.forEach(agendamento -> funcionariosList.add(agendamento.getFuncionario()));

        funcionariosList.forEach(funcionario -> {

            List<Empresa> empresasList = funcionario.getEmpresas();

            empresasList.forEach(empresa -> {

                List<Contato> contatosList = empresa.getContatos();

                contatosList.forEach(contato ->
                        this.enviarEmailExameExpirando(funcionario, contato.getEmail()));
            });

            this.enviarEmailExameExpirando(funcionario, funcionario.getEmail());
        });
    }

    @Scheduled(cron = "0 16 * * * *")
    public void avisoPoliciaFederal() {

        LocalDate hoje = LocalDate.now();

        List<Agendamento> agendamentosList = this.agendamentoRepository.findAllByAgendaDiaAgenda(hoje.plusDays(3L));
        List<Funcionario> funcionariosList = new ArrayList<>();

        agendamentosList.stream()
                .filter(Agendamento::getTrabalhoArmado)
                .forEach(agendamento -> funcionariosList.add(agendamento.getFuncionario()));

        this.enviarEmailPoliciaFederal(funcionariosList);
    }

    @Scheduled(cron = "0 1 1 * * *")
    public void emailMensalEmpresas() {

        LocalDate hoje = LocalDate.now();
        LocalDate earlier = hoje.minusMonths(1); // 01-11-2018

        Integer lastMonth = earlier.getMonth().getValue(); // java.time.Month = 11
        Integer year = earlier.getYear(); // 2018

        List<Agendamento> agendamentosList =
                this.agendamentoRepository.findAllByAgendaDiaAgendaMonthAndAgendaDiaAgendaYear(lastMonth, year);

        //TODO : Buscar todos os exames do mês anterior
    }

    private Boolean diaDeAniversarioHoje(Funcionario funcionario, LocalDate hoje) {

        LocalDate dataNascimento = funcionario.getDataNascimento();

        return (dataNascimento.getDayOfMonth() == hoje.getDayOfMonth()) && (dataNascimento.getMonth() == hoje.getMonth());
    }

    private void enviarEmailAniversario(Funcionario funcionario) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", funcionario.getNome());

        this.envioEmail.enviarEmail(emailIdeia,
                Collections.singletonList(funcionario.getEmail()),
                "O Sistema Ideia deseja um feliz aniversário para você", "email/aniversario", map);
    }

    private void enviarEmailExameExpirando(Funcionario funcionario, String emailEmpresa) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", funcionario.getNome());

        this.envioEmail.enviarEmail(emailIdeia,
                Collections.singletonList(emailEmpresa),
                "O Sistema Ideia - Exame expirando",
                "email/exame-expirando", map);
    }

    private void enviarEmailPoliciaFederal(List<Funcionario> funcionariosList) {
        Map<String, Object> map = new HashMap<>();
        List<String> nomesFuncionarios = new ArrayList<>();

        funcionariosList.forEach(funcionario -> nomesFuncionarios.add(funcionario.getNome()));
        map.put("names", nomesFuncionarios);

        this.envioEmail.enviarEmail(emailIdeia,
                Collections.singletonList(emailPoliciaFederal),
                "Agendamentos de exames psicologicos para trabalhos armados",
                "email/policia-federal", map);
    }

}
