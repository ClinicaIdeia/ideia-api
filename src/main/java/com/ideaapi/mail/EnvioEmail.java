package com.ideaapi.mail;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Component
public class EnvioEmail {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private TemplateEngine thymeleaf;
//
//    @EventListener
//    private void testeEmail(ApplicationReadyEvent event) {
//
//        String template = "email/envio-senha";
//
//        Map<String, Object> map = new HashMap<>();
//        map.put("senha", 123456);
//
//        this.enviarEmail("openlinkti@gmail.com", Arrays.asList("alvesdesouzaalex@gmail.com"), "teste", template, map);
//
//    }

    public void enviarEmail(String remetente, List<String> destinatarios, String assunto, String template,
            Map<String, Object> variaveis) {


        Context context = new Context(new Locale("pt-BR"));

        variaveis.entrySet().forEach(e -> context.setVariable(e.getKey(), e.getValue()));

        String mensagem = this.thymeleaf.process(template, context);
        this.enviarEmail(remetente, destinatarios, assunto, mensagem);

    }

    private void enviarEmail(String remetente, List<String> destinatarios, String assunto, String mensagem) {

        try {

            MimeMessage mimeMessage = this.javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
            helper.setFrom(remetente);
            helper.setTo(destinatarios.toArray(new String[destinatarios.size()]));
            helper.setSubject(assunto);
            helper.setText(mensagem, true);

            this.javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException("Erro ao enviar email");
        }

    }
}
