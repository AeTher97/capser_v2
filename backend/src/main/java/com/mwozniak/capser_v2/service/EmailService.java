package com.mwozniak.capser_v2.service;

import com.mwozniak.capser_v2.models.database.FailedEmail;
import com.mwozniak.capser_v2.repository.FailedEmailRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@Log4j2
public class EmailService {

    private final JavaMailSender emailSender;
    private final ExecutorService executorService = Executors.newFixedThreadPool(2);
    private final FailedEmailRepository failedEmailRepository;

    public EmailService(JavaMailSender emailSender, FailedEmailRepository failedEmailRepository) {
        this.failedEmailRepository = failedEmailRepository;
        log.info("Using email account " + System.getenv("EMAIL_USERNAME") + " to send notifications");
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
        log.info("Sending html email to " + to);
        executorService.submit(new SendMessageTask(to, subject, content, emailSender, this));
    }

    @Scheduled(cron = "0 0 21 * * *")
    public void resendEmails() {
        log.info("Resending failed emails");
        List<FailedEmail> failedEmailList = failedEmailRepository.findAll();
        log.info(failedEmailList.size() + " failed emails found");
        failedEmailList.forEach(failedEmail -> {
            sendHtmlMessage(failedEmail.getRecipient(), failedEmail.getSubject(), failedEmail.getContent());
            failedEmailRepository.delete(failedEmail);
        });
    }

    public void saveFailedEmail(FailedEmail failedEmail) {
        failedEmailRepository.save(failedEmail);
    }

    @AllArgsConstructor
    private static class SendMessageTask implements Runnable {

        private final String to;
        private final String subject;
        private final String content;
        private final JavaMailSender emailSender;
        private final EmailService emailService;

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
                log.error("Failed sending email " + e.getMessage());
                emailService.saveFailedEmail(FailedEmail.builder()
                        .recipient(to)
                        .subject(subject)
                        .content(content)
                        .build());
            }
        }
    }
}
