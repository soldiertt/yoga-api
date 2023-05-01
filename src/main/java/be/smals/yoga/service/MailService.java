package be.smals.yoga.service;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender emailSender;

    public void sendSimpleMessage(final String to, final String subject, final String htmlContent) throws MessagingException {
//        final var message = emailSender.createMimeMessage();
//        message.setFrom(new InternetAddress("yogaenpevele@gmail.com"));
//        message.setRecipients(MimeMessage.RecipientType.TO, to);
//        message.setSubject(subject);
//        message.setContent(htmlContent, "text/html; charset=utf-8");
//        emailSender.send(message);
    }
}
