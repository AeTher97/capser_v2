package com.mwozniak.capser_v2.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mwozniak.capser_v2.models.database.FailedEmail;
import com.mwozniak.capser_v2.repository.FailedEmailRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@Log4j2
public class EmailService {

    private final ExecutorService executorService = Executors.newFixedThreadPool(2);
    private final FailedEmailRepository failedEmailRepository;
    private final JavaMailSender javaMailSender;

    public EmailService(FailedEmailRepository failedEmailRepository, JavaMailSender javaMailSender) {
        this.failedEmailRepository = failedEmailRepository;
        this.javaMailSender = javaMailSender;
        log.info("Using email account " + System.getenv("EMAIL_USERNAME") + " to send notifications");
    }


    public void sendHtmlMessage(String to, String subject, String content) {
        log.info("Sending html email to " + to);
        try {
            executorService.submit(new SendMessageTask(javaMailSender, to, subject, content, this));
        } catch (Exception e) {
            //don't propagate this exception to callers to avoid weird errors, save email for later and handle it here
            log.error("Failed to send email " + e.getMessage());
            saveFailedEmail(FailedEmail.builder()
                    .recipient(to)
                    .subject(subject)
                    .content(content)
                    .date(new Date())
                    .build());
        }
    }

    @Scheduled(cron = "${time.series.cron}")
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

        private final JavaMailSender javaMailSender;
        private final String to;
        private final String subject;
        private final String content;
        private final EmailService emailService;

        @Override
        public void run() {
            if (to == null) {
                return;
            }
            try {
                Properties props = System.getProperties();
                props.put("mail.transport.protocol", "smtp");
                props.put("mail.smtp.port", 587);
                props.put("mail.smtp.starttls.enable", "true");

                Session session = Session.getDefaultInstance(props);

                MimeMessage mimeMessage = new MimeMessage(session);
                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
                helper.setText(content, true);
                helper.setTo(to);
                helper.setSubject(subject);
                helper.setFrom("globalcapsleague@gmail.com");

                javaMailSender.send(mimeMessage);
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
