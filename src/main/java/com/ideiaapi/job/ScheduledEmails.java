package com.ideiaapi.job;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ideiaapi.mail.EnvioEmail;
import com.ideiaapi.model.Agendamento;
import com.ideiaapi.model.Contato;
import com.ideiaapi.model.Empresa;
import com.ideiaapi.model.Exame;
import com.ideiaapi.model.Funcionario;
import com.ideiaapi.repository.AgendamentoRepository;
import com.ideiaapi.repository.FuncionarioRepository;

@Component
public class ScheduledEmails {

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private AgendamentoRepository agendamentoRepository;

    @Autowired
    private EnvioEmail envioEmail;

    //TODO parametrizer e-mails
    private static String emailPoliciaFederal = "psicologos.deleaq.mg@dpf.gov.br";
    private static String emailIdeia = "clinica.ideia@gmail.com";
    private static String emailNilza = "nilzamarquez5@gmail.com";

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

        List<Agendamento> agendamentosList = this.agendamentoRepository.findAllByDate(hoje.minusDays(335L));
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

        List<Agendamento> agendamentosList = this.agendamentoRepository.findAllByDate(hoje.plusDays(3L));
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

        Integer lastMonth = earlier.getMonth().getValue(); // 11
        Integer year = earlier.getYear(); // 2018

        List<Agendamento> agendamentosList = this.agendamentoRepository.findAllByMonthAndYear(lastMonth, year);

        this.enviarRelatorioPorEmpresa(agendamentosList);
    }

    private void enviarRelatorioPorEmpresa(List<Agendamento> agendamentosList) {
        HashMap<Funcionario, LocalDate> dataAgendaPorFuncionario = new HashMap<>();
        List<Funcionario> listaDeTodosOsFuncionarios = new ArrayList<>();
        List<Empresa> listaDeTodasAsEmpresas = new ArrayList<>();
        List<Funcionario> listaDosFuncionariosDaEmpresa = new ArrayList<>();
        List<Exame> listaDosExamesDaEmpresa = new ArrayList<>();

        agendamentosList.forEach(
                agendamento -> {
                    Funcionario funcionario = agendamento.getFuncionario();
                    listaDeTodosOsFuncionarios.add(funcionario);
                    LocalDate diaDoExame = agendamento.getAgenda().getDiaAgenda();
                    dataAgendaPorFuncionario.put(funcionario, diaDoExame);
                }
        );

        listaDeTodosOsFuncionarios.forEach(
                funcionario -> {
                    List<Empresa> empresasDoFuncionario = funcionario.getEmpresas();
                    empresasDoFuncionario.forEach(
                            empresa -> {
                                if (!listaDeTodasAsEmpresas.contains(empresa))
                                    listaDeTodasAsEmpresas.add(empresa);
                            }
                    );
                }
        );

        listaDeTodasAsEmpresas.forEach(
                empresa -> {
                    listaDeTodosOsFuncionarios.forEach(
                            funcionario -> {
                                if (funcionario.getEmpresas().contains(empresa))
                                    listaDosFuncionariosDaEmpresa.add(funcionario);
                            }
                    );

                    listaDosFuncionariosDaEmpresa.forEach(
                            funcionario -> {
                                LocalDate dataDoExameDoFuncionario = dataAgendaPorFuncionario.get(funcionario);
                                listaDosExamesDaEmpresa.add(new Exame(funcionario.getNome(), dataDoExameDoFuncionario));
                            }
                    );

                    //TODO descomentar quando estiver em PROD, 
                   // this.enviarRelatorioDosExamesDeMes(empresa.getNome(), listaDosExamesDaEmpresa);
                }
        );
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

    private void enviarRelatorioDosExamesDeMes(String empresa, List<Exame> listaDosExamesDaEmpresa) {
        Map<String, Object> map = new HashMap<>();
        map.put("empresa", empresa);
        map.put("exames", listaDosExamesDaEmpresa);


        this.envioEmail.enviarEmail(emailNilza,
                Collections.singletonList(emailPoliciaFederal),
                "Relatorio mensal de exames psicologicos",
                "email/relatorio-mensal-por-empresa", map);
    }

}
