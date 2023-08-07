package com.aea.authservice.common;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EmailUtils {

    private final JavaMailSender mailSender;

    @Async
    public void sendConfirmationEmail(String to, String url) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(this.getConfirmationTemplate(url), true);
            helper.setTo(to);
            helper.setSubject("Confirm your email");
            helper.setFrom("hello@aea.com");
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new IllegalStateException("failed to send email");
        }
    }

    @Async
    public void sendPwResetEmail(String to, String url) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(this.getPasswordResetTemplate(url), true);
            helper.setTo(to);
            helper.setSubject("Reset your password");
            helper.setFrom("help@aea.com");
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new IllegalStateException("failed to send email");
        }
    }

    private String getConfirmationTemplate(String verificationUrl) {
        return "<h3>Click link to activate your account.</h3>\n" +
                "<p><a href ="+ verificationUrl+">Link</a></p>";
    }

    private String getPasswordResetTemplate(String url) {
        return "<h3>Click link to reset your password.</h3>\n" +
                "<p><a href ="+ url +">Link</a></p>";
    }
}
