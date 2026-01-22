package com.tierranativa.aplicacion.tierra.nativa.service;

import com.tierranativa.aplicacion.tierra.nativa.exception.EmailNotificationException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Async
    public void sendWelcomeEmail(String to, String firstName, String lastName, String token) {
        log.info("Iniciando proceso de envío de email a: {}", to);

        try {
            Context context = new Context();

            String verifyUrl = "http://localhost:5173/verify-email?token=" + token;
            String loginUrl = "http://localhost:5173/login";

            context.setVariable("nombre", firstName);
            context.setVariable("apellido", lastName);
            context.setVariable("email", to);
            context.setVariable("loginUrl", loginUrl);
            context.setVariable("verifyUrl", verifyUrl);

            String htmlContent = templateEngine.process("welcome-email", context);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(
                    message,
                    true,
                    StandardCharsets.UTF_8.name()
            );

            helper.setTo(to);
            helper.setSubject("¡Bienvenido a Tierra Nativa! - Confirma tu correo");
            helper.setFrom("Tierra Nativa <no-reply@tierranativa.com>");
            helper.setText(htmlContent, true);

            ClassPathResource logoResource = new ClassPathResource("static/images/logo.png");
            if (logoResource.exists()) {
                helper.addInline("logoTierra", logoResource);
            } else {
                log.warn("Archivo de logo no encontrado en: static/images/logo.png");
            }

            mailSender.send(message);
            log.info("Email enviado exitosamente a: {}", to);

        } catch (MessagingException | MailException e) {
            log.error("Error al enviar el correo electrónico a {}: {}", to, e.getMessage());
            throw new EmailNotificationException("Fallo en la notificación de correo para el usuario: " + to, e);
        }
    }
}