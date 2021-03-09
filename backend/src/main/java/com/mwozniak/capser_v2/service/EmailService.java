package com.mwozniak.capser_v2.service;

import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class EmailService {

    private final JavaMailSender emailSender;
    private final ExecutorService executorService = Executors.newFixedThreadPool(2);

    public EmailService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    public void sendSimpleMessage(
            String to, String subject, String text) {
        if (to == null) {
            return;
        }
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("globalcapsleague@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);

    }

    public void sendHtmlMessage(String to, String subject, String content) {
        executorService.submit(new SendMessageTask(to, subject, content, emailSender));
    }

    @AllArgsConstructor
    private static class SendMessageTask implements Runnable {

        private final String to;
        private final String subject;
        private final String content;
        private final JavaMailSender emailSender;

        @Override
        public void run() {
            if (to == null) {
                return;
            }
            try {
                MimeMessage mimeMessage = emailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
                helper.setText(content, true);
                helper.setTo(to);
                helper.setSubject(subject);
                helper.setFrom("globalcapsleague@gmail.com");
                emailSender.send(mimeMessage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
