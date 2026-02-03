package net.foodeals.common.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Transactional
    public void sendEmail(String to, String subject, String htmlText) {
        String recipient = to == null ? null : to.trim();
        if (recipient == null || recipient.isEmpty()) {
            throw new RuntimeException("Failed to send HTML email: recipient is empty");
        }
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setTo(new InternetAddress(recipient, true));
            helper.setSubject(subject);
            helper.setText(htmlText, true); // true indicates HTML content

            log.info("Sending validation email to '{}'", recipient);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            // Surface root cause for easier debugging
            throw new RuntimeException("Failed to send HTML email to '" + recipient + "': " + e.getMessage(), e);
        }
    }
}
