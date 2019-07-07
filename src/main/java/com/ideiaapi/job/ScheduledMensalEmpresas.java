package com.ideiaapi.job;

import com.ideiaapi.mail.EnvioEmail;
import com.ideiaapi.model.Exame;
import com.ideiaapi.repository.AgendamentoRepository;
import com.ideiaapi.util.datas.UtilsData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

@Component
public class ScheduledMensalEmpresas {

    private static String emailIdeia = "clinica.ideia@gmail.com";
    private static String emailNilza = "nilzamarquez5@gmail.com";
    private static String auditor = "alvesdesouzaalex@gmail.com";
    private static String PATTERN_DATE = "dd-MM-yyyy";

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
    @Scheduled(fixedDelay = 3000)
    public void emailMensalEmpresas() {

        LocalDate ultimoDiaMesAnterior = LocalDate.now().minusDays(30);
        LocalDate primeitoDia = ultimoDiaMesAnterior.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate ultimoDia = ultimoDiaMesAnterior.with(TemporalAdjusters.lastDayOfMonth());

        SortedSet<String> empresas = new TreeSet<>();
        Map<String, Set<Exame>> examesMap = new HashMap<>();

        this.agendamentoRepository.findAllByAgenda_DiaAgenda_Day(primeitoDia, ultimoDia).forEach(agendamento -> {

            final String empresa = agendamento.getEmpresa().getNome();
            empresas.add(empresa);

            if (examesMap.containsKey(empresa)) {

                examesMap.get(empresa).add(new Exame(agendamento.getFuncionario().getNome(), UtilsData.getDataConvertida(agendamento.getAgenda().getDiaAgenda(), PATTERN_DATE)));

            } else {

                Set<Exame> exames = new HashSet<>();
                exames.add(new Exame(agendamento.getFuncionario().getNome(), UtilsData.getDataConvertida(agendamento.getAgenda().getDiaAgenda(), PATTERN_DATE)));
                examesMap.put(empresa, exames);

            }

        });

        if (!examesMap.isEmpty() && !empresas.isEmpty()) {

            empresas.forEach(emp -> {
                final Set<Exame> exs = examesMap.get(emp);
                if (!exs.isEmpty()) {
                    this.enviarRelatorioDosExamesDeMes(emp, exs);
                }
            });
        }

        empresas.clear();
    }

    private void enviarRelatorioDosExamesDeMes(String empresa, Set<Exame> listaDosExamesDaEmpresa) {
        Map<String, Object> map = new HashMap<>();
        map.put("empresa", empresa);
        map.put("exames", listaDosExamesDaEmpresa);

        this.envioEmail.enviarEmail(emailNilza,
                Arrays.asList(emailIdeia, emailIdeia, auditor),
                "Clinica Ideia - Relatorio mensal de exames psicologicos",
                "email/relatorio-mensal-por-empresa", map);
    }
}
