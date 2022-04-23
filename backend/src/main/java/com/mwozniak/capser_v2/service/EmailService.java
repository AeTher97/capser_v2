package com.mwozniak.capser_v2.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mwozniak.capser_v2.configuration.EmailConfiguration;
import com.mwozniak.capser_v2.models.database.FailedEmail;
import com.mwozniak.capser_v2.repository.FailedEmailRepository;
import com.sun.mail.smtp.SMTPTransport;
import com.sun.mail.util.BASE64EncoderStream;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@Log4j2
public class EmailService {

    private final ExecutorService executorService = Executors.newFixedThreadPool(2);
    private final FailedEmailRepository failedEmailRepository;
    private final EmailConfiguration emailConfiguration;
    private final RestTemplate restTemplate = new RestTemplate();
    private long lastRefresh;
    private String accessToken;

    public EmailService(FailedEmailRepository failedEmailRepository, EmailConfiguration emailConfiguration) {
        this.failedEmailRepository = failedEmailRepository;
        this.emailConfiguration = emailConfiguration;
        log.info("Using email account " + System.getenv("EMAIL_USERNAME") + " to send notifications");
    }

    private void refreshToken() {
        String refreshToken = emailConfiguration.getToken();

        Map<String, String> params = new HashMap<>();
        params.put("client_id", emailConfiguration.getClientId());
        params.put("client_secret", emailConfiguration.getClientSecret());
        params.put("refresh_token", refreshToken);
        params.put("grant_type", "refresh_token");
        TokenResponse tokenResponse = restTemplate.postForEntity("https://accounts.google.com/o/oauth2/token", params, TokenResponse.class).getBody();
        assert tokenResponse != null;

        accessToken = tokenResponse.getAccessToken();
        lastRefresh = new Date().getTime() + 20000;

    }

    private void updateTokenIfNecessary() {
        if (new Date().getTime() - lastRefresh > 3600 * 1000) {
            refreshToken();
        }
    }


    public void sendHtmlMessage(String to, String subject, String content) {
        log.info("Sending html email to " + to);
        updateTokenIfNecessary();
        executorService.submit(new SendMessageTask(to, subject, content, this, emailConfiguration));
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
        private final EmailService emailService;
        private final EmailConfiguration emailConfiguration;

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

                SMTPTransport transport = new SMTPTransport(session, null);
                transport.connect("smtp.gmail.com", emailConfiguration.getUsername(), null);
                transport.issueCommand("AUTH XOAUTH2 " + new String(BASE64EncoderStream.encode(String.format("user=%s\1auth=Bearer %s\1\1", emailConfiguration.getUsername(), emailService.accessToken).getBytes())), 235);

                transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
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

    @Data
    private static class TokenResponse {

        @JsonProperty("access_token")
        private String accessToken;


    }
}
