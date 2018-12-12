package com.ideaapi.job;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ideaapi.mail.EnvioEmail;
import com.ideaapi.model.Agendamento;
import com.ideaapi.model.Contato;
import com.ideaapi.model.Empresa;
import com.ideaapi.model.Funcionario;
import com.ideaapi.repository.AgendamentoRepository;
import com.ideaapi.repository.FuncionarioRepository;

@Component
public class ScheduledEmails {

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private AgendamentoRepository agendamentoRepository;

    @Autowired
    private EnvioEmail envioEmail;

    private static String EMAIL_POLICIA_FEDERAL = "psicologos.deleaq.mg@dpf.gov.br";

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

        List<Agendamento> agendamentosList = this.agendamentoRepository.findAllByAgenda_DiaAgenda(hoje.minusDays(335L));
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

        List<Agendamento> agendamentosList = this.agendamentoRepository.findAllByAgenda_DiaAgenda(hoje.plusDays(3L));
        List<Funcionario> funcionariosList = new ArrayList<>();

        agendamentosList.stream()
                .filter(Agendamento::getTrabalhoArmado)
                .forEach(agendamento -> funcionariosList.add(agendamento.getFuncionario()));

        this.enviarEmailPoliciaFederal(funcionariosList);
    }

    @Scheduled(cron = "0 1 1 * * *")
    public void avisoEmpresas() {

        LocalDate hoje = LocalDate.now();
        LocalDate earlier = hoje.minusMonths(1); // 01-11-2018

        Month lastMonth = earlier.getMonth(); // java.time.Month = NOVEMBER
        Integer year = earlier.getYear(); // 2018

        List<Agendamento> agendamentosList =
                this.agendamentoRepository.findAllByAgenda_DiaAgenda_MonthAndAgenda_DiaAgenda_Year(lastMonth, year);


        //TODO : Buscar todos os exames do mês anterior
    }

    private Boolean diaDeAniversarioHoje(Funcionario funcionario, LocalDate hoje) {

        LocalDate dataNascimento = funcionario.getDataNascimento();

        return (dataNascimento.getDayOfMonth() == hoje.getDayOfMonth()) && (dataNascimento.getMonth() == hoje.getMonth());
    }

    private void enviarEmailAniversario(Funcionario funcionario) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", funcionario.getNome());

        this.envioEmail.enviarEmail("openlinkti@gmail.com",
                Collections.singletonList(funcionario.getEmail()),
                "O Sistema Ideia deseja um feliz aniversário para você", "email/aniversario", map);
    }

    private void enviarEmailExameExpirando(Funcionario funcionario, String emailEmpresa) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", funcionario.getNome());

        this.envioEmail.enviarEmail("openlinkti@gmail.com",
                Collections.singletonList(emailEmpresa),
                "O Sistema Ideia - Exame expirando",
                "email/exame-expirando", map);
    }

    private void enviarEmailPoliciaFederal(List<Funcionario> funcionariosList) {
        Map<String, Object> map = new HashMap<>();
        AtomicInteger i = new AtomicInteger(1);

        funcionariosList.forEach(funcionario -> {
            String name = "funcionario" + i;
            map.put(name, funcionario.getNome());
            i.getAndIncrement();
        });

        this.envioEmail.enviarEmail("openlinkti@gmail.com",
                Collections.singletonList(EMAIL_POLICIA_FEDERAL),
                "Agendamentos de exames psicologicos para trabalhos armados",
                "email/policia-federal", map);
    }

}
