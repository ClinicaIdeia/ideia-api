package com.ideiaapi.job;

import com.ideiaapi.mail.EnvioEmail;
import com.ideiaapi.model.*;
import com.ideiaapi.repository.AgendamentoRepository;
import com.ideiaapi.repository.FuncionarioRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
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
    private static String emailNilza = "nilzamarquez5@gmail.com";


    /**
     * EXAME EXPIRANDO
     */

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
                contatosList.forEach(
                        contato -> this.enviarEmailExameExpirando(funcionario, contato.getEmail())
                );
            });

            if (StringUtils.isNotBlank(funcionario.getEmail()))
                this.enviarEmailExameExpirando(funcionario, funcionario.getEmail());
        });
    }

    private void enviarEmailExameExpirando(Funcionario funcionario, String emailEmpresa) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", funcionario.getNome());

        this.envioEmail.enviarEmail(emailIdeia,
                Collections.singletonList(emailEmpresa),
                "Clinica Ideia - Exame expirando",
                "email/exame-expirando", map);
    }

    /**
     * POLICIA FEDERAL
     */

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

    /**
     * EMPRESA (MENSAL)
     */

    @Scheduled(cron = "0 1 1 * * *")
    public void emailMensalEmpresas() {

        LocalDate hoje = LocalDate.now();
        LocalDate earlier = hoje.minusMonths(1); // 01-11-2018

        Integer lastMonth = earlier.getMonth().getValue(); // java.time.Month = 11
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

                    this.enviarRelatorioDosExamesDeMes(empresa.getNome(), listaDosExamesDaEmpresa);
                }
        );
    }

    private void enviarRelatorioDosExamesDeMes(String empresa, List<Exame> listaDosExamesDaEmpresa) {
        Map<String, Object> map = new HashMap<>();
        map.put("empresa", empresa);
        map.put("exames", listaDosExamesDaEmpresa);

        this.envioEmail.enviarEmail(emailNilza,
//        Collections.singletonList(emailPoliciaFederal),
//      TODO : Colocar de volta quando enviar diretamente para Policia Federal
                Collections.singletonList(emailNilza),
                "Clinica Ideia - Relatorio mensal de exames psicologicos",
                "email/relatorio-mensal-por-empresa", map);
    }

    /**
     * ANO NOVO
     */

    @Scheduled(cron = "0 0 6 1 1 *")
    public void anoNovo() {

        List<Funcionario> funcionarioList = this.funcionarioRepository.findAll();

        funcionarioList.stream()
                .filter(funcionario -> StringUtils.isNotBlank(funcionario.getEmail()))
                .forEach(this::enviarEmailAnoNovo);
    }

    private void enviarEmailAnoNovo(Funcionario funcionario) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", funcionario.getNome());

        this.envioEmail.enviarEmail(emailIdeia,
                Collections.singletonList(funcionario.getEmail()),
                "Feliz Ano Novo",
                "email/ano-novo", map);
    }

    /**
     * DIA DO AMIGO
     */

    @Scheduled(cron = "0 0 6 20 7 *")
    public void diaAmigo() {

        List<Funcionario> funcionarioList = this.funcionarioRepository.findAll();

        funcionarioList.stream()
                .filter(funcionario -> StringUtils.isNotBlank(funcionario.getEmail()))
                .forEach(this::enviarEmailDiaAmigo);
    }

    private void enviarEmailDiaAmigo(Funcionario funcionario) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", funcionario.getNome());

        this.envioEmail.enviarEmail(emailIdeia,
                Collections.singletonList(funcionario.getEmail()),
                "Feliz Dia do Amigo",
                "email/dia-amigo", map);
    }

    /**
     * DIA DAS MULHERES
     */

    @Scheduled(cron = "0 0 6 8 3 *")
    public void diaMulheres() {

        List<Funcionario> funcionarioList = this.funcionarioRepository.findAll();

        funcionarioList.stream()
                .filter(funcionario -> StringUtils.isNotBlank(funcionario.getEmail())
                        && StringUtils.isNotBlank(funcionario.getSexo())
                        && "F".equalsIgnoreCase(funcionario.getSexo()))
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

    /**
     * DIA DO TRABALHADOR
     */

    @Scheduled(cron = "0 0 6 1 5 *")
    public void diaTrabalhador() {

        List<Funcionario> funcionarioList = this.funcionarioRepository.findAll();

        funcionarioList.stream()
                .filter(funcionario -> StringUtils.isNotBlank(funcionario.getEmail()))
                .forEach(this::enviarEmailDiaTrabalhador);
    }

    private void enviarEmailDiaTrabalhador(Funcionario funcionario) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", funcionario.getNome());

        this.envioEmail.enviarEmail(emailIdeia,
                Collections.singletonList(funcionario.getEmail()),
                "Feliz Dia do Trabalhador",
                "email/dia-trabalhador", map);
    }

    /**
     * NATAL
     */

    @Scheduled(cron = "0 0 6 25 12 *")
    public void natal() {

        List<Funcionario> funcionarioList = this.funcionarioRepository.findAll();

        funcionarioList.stream()
                .filter(funcionario -> StringUtils.isNotBlank(funcionario.getEmail()))
                .forEach(this::enviarEmailNatal);
    }

    private void enviarEmailNatal(Funcionario funcionario) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", funcionario.getNome());

        this.envioEmail.enviarEmail(emailIdeia,
                Collections.singletonList(funcionario.getEmail()),
                "Feliz Natal",
                "email/natal", map);
    }
}
