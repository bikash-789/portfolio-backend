package com.bikash.portfolio_backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import jakarta.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${spring.mail.from}")
    private String fromEmail;

    @Value("${cors.allowed-origins}")
    private String allowedOrigins;

    public void sendEmailVerification(String toEmail, String token) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("Verify Your Email Address");

            String verificationUrl = getBaseUrl() + "/auth/verify-email?token=" + token;
            Context context = new Context();
            context.setVariable("verificationUrl", verificationUrl);
            String htmlContent = templateEngine.process("email-verification.html", context);
            helper.setText(htmlContent, true);

            mailSender.send(message);
            log.info("Email verification sent to: {}", toEmail);
        } catch (Exception e) {
            log.error("Failed to send email verification to: {}", toEmail, e);
        }
    }

    public void sendPasswordResetEmail(String toEmail, String token) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Reset Your Password");

            String resetUrl = getBaseUrl() + "/auth/reset-password?token=" + token;
            message.setText("Please click the following link to reset your password:\n\n" + resetUrl +
                          "\n\nThis link will expire in 1 hour.");

            mailSender.send(message);
            log.info("Password reset email sent to: {}", toEmail);
        } catch (Exception e) {
            log.error("Failed to send password reset email to: {}", toEmail, e);
        }
    }

    public void sendContactNotification(String adminEmail, String contactName, String contactEmail, String subject) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(adminEmail);
            message.setSubject("New Contact Form Submission");

            message.setText("You have received a new contact form submission:\n\n" +
                          "Name: " + contactName + "\n" +
                          "Email: " + contactEmail + "\n" +
                          "Subject: " + subject + "\n\n" +
                          "Please log in to your admin panel to view the full message.");

            mailSender.send(message);
            log.info("Contact notification sent to admin: {}", adminEmail);
        } catch (Exception e) {
            log.error("Failed to send contact notification to admin: {}", adminEmail, e);
        }
    }

    private String getBaseUrl() {
        String[] origins = allowedOrigins.split(",");
        return origins[0].trim();
    }
} 