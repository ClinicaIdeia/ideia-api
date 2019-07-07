package com.ideiaapi.job;

import com.ideiaapi.mail.EnvioEmail;
import com.ideiaapi.model.Agendamento;
import com.ideiaapi.model.Empresa;
import com.ideiaapi.model.Exame;
import com.ideiaapi.model.Funcionario;
import com.ideiaapi.repository.AgendamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;

@Component
public class ScheduledMensalEmpresas {

    private static String emailPoliciaFederal = "psicologos.deleaq.mg@dpf.gov.br";
    private static String emailIdeia = "clinica.ideia@gmail.com";
    private static String emailNilza = "nilzamarquez5@gmail.com";

    @Autowired
    private AgendamentoRepository agendamentoRepository;

    @Autowired
    private EnvioEmail envioEmail;

    /**
     * EMPRESA (MENSAL)
     */
    /***
     * A: Segundos (0 – 59).
     * B: Minutos (0 – 59).
     * C: Horas (0 – 23).
     * D: Dia (1 – 31).
     * E: Mês (1 – 12).
     * F: Dia da semana (0 – 6).
     */
//    @Scheduled(cron = "0 1 1 * * *")
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
}
