package com.ideiaapi.job;

import com.ideiaapi.mail.EnvioEmail;
import com.ideiaapi.model.Agendamento;
import com.ideiaapi.repository.AgendamentoRepository;
import com.ideiaapi.util.datas.UtilsData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class ScheduledPoliciaFederal {

    private static final String EMAIL_PF = "psicologos.deleaq.mg@dpf.gov.br";
    private static final String EMAIL_IDEIA = "clinica.ideia@gmail.com";
    private static final String EMAIL_NILZA = "nilzamarquez5@gmail.com";
    private static final String AUDITOR = "alvesdesouzaalex@gmail.com";

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

    @Scheduled(cron = "0 0 18 * * *")
//    @Scheduled(fixedDelay = 3000)
    public void avisoPoliciaFederal() {

        LocalDate dtaExame = LocalDate.now().plusDays(3L);

        List<Agendamento> agendamentos = this.agendamentoRepository.findAllByDateAAndAgenda_DiaAgenda(dtaExame);

        if (!agendamentos.isEmpty()) {
            this.enviarEmailPoliciaFederal(agendamentos, dtaExame);
        }
    }

    private void enviarEmailPoliciaFederal(List<Agendamento> agendamentos, LocalDate dataAgenda) {
        Map<String, Object> map = new HashMap<>();
        List<FuncionarioPF> funcs = new ArrayList<>();

        agendamentos.forEach(agendamento ->
                funcs.add(new FuncionarioPF(agendamento.getHoraExame().toString(), agendamento.getFuncionario().getNome(),
                        agendamento.getFuncionario().getCpf(), agendamento.getFuncionario().getTelefone())));

        map.put("diaAgenda", UtilsData.getDataConvertida(dataAgenda, "dd/MM/yyyy"));
        map.put("diaSemana", UtilsData.getDiaDaSemanaFormatado(dataAgenda));
        map.put("funcs", funcs);

        String assunto = "Clinica Ideia - Agenda de Atendimento dia: " + UtilsData.getDataConvertida(dataAgenda, "dd-MM-yyyy");

        this.envioEmail.enviarEmail(EMAIL_IDEIA,
//                Collections.singletonList(EMAIL_PF),
//                TODO : Colocar de volta quando enviar diretamente para Policia Federal
//                Arrays.asList(EMAIL_NILZA, EMAIL_IDEIA, AUDITOR),
                Arrays.asList(AUDITOR),
                assunto,
                "email/policia-federal", map);
    }


    private class FuncionarioPF {
        private String hora;
        private String nome;
        private String cpf;
        private String telefone;

        public FuncionarioPF(String hora, String nome, String cpf, String telefone) {
            this.hora = hora;
            this.nome = nome;
            this.cpf = cpf;
            this.telefone = telefone;
        }

        public String getHora() {
            return hora;
        }

        public void setHora(String hora) {
            this.hora = hora;
        }

        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }

        public String getCpf() {
            return cpf;
        }

        public void setCpf(String cpf) {
            this.cpf = cpf;
        }

        public String getTelefone() {
            return telefone;
        }

        public void setTelefone(String telefone) {
            this.telefone = telefone;
        }
    }

}
