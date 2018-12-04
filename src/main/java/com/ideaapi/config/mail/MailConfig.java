package com.ideaapi.config.mail;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class MailConfig {

    @Value("${ideia-api.mail.host}")
    private String host;

    @Value("${ideia-api.mail.port}")
    private Integer port;

    @Value("${ideia-api.mail.username}")
    private String username;

    @Value("${ideia-api.mail.password}")
    private String password;


    @Bean
    public JavaMailSender javaMailSender() {

        Properties props = new Properties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", true);
        props.put("mail.smtp.starttls.enable", true);
        props.put("mail.smtp.connectiontimeout", 10000);

        JavaMailSenderImpl impl = new JavaMailSenderImpl();
        impl.setJavaMailProperties(props);
        impl.setHost(host);
        impl.setPort(port);
        impl.setUsername(username);
        impl.setPassword(password);

        return impl;

    }

}
