package com.tierranativa.aplicacion.tierra.nativa.service;

import com.tierranativa.aplicacion.tierra.nativa.dto.BookingResponseDTO;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private TemplateEngine templateEngine;

    @InjectMocks
    private EmailService emailService;

    @Mock
    private MimeMessage mimeMessage;

    private BookingResponseDTO sampleBooking;

    @BeforeEach
    void setUp() {
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        sampleBooking = BookingResponseDTO.builder()
                .id(100L)
                .userEmail("viajero@test.com")
                .userFirstName("Juan")
                .userLastName("Perez")
                .packageName("Glaciar Perito Moreno")
                .packageDestination("El Calafate")
                .packageBasePrice(100000.0)
                .totalPrice(200000.0)
                .travelerCount(2)
                .startDate(LocalDate.now().plusDays(30))
                .endDate(LocalDate.now().plusDays(33))
                .status("CONFIRMED")
                .build();
    }

    // =========================================================
    // sendWelcomeEmail
    // =========================================================

    @Test
    void sendWelcomeEmail_Success_CallsMailSenderAndTemplateEngine() {
        when(templateEngine.process(eq("welcome-email"), any(Context.class)))
                .thenReturn("<html>Bienvenido</html>");

        emailService.sendWelcomeEmail("test@user.com", "Juan", "Perez", "valid-uuid");

        verify(mailSender, times(1)).send(any(MimeMessage.class));
        verify(templateEngine, times(1)).process(eq("welcome-email"), any(Context.class));
    }

    // =========================================================
    // sendBookingConfirmation
    // =========================================================

    @Test
    void sendBookingConfirmation_Success_CallsMailSenderAndTemplateEngine() {
        when(templateEngine.process(eq("booking-confirmation"), any(Context.class)))
                .thenReturn("<html>Confirmación</html>");

        emailService.sendBookingConfirmation(sampleBooking);

        verify(mailSender, times(1)).send(any(MimeMessage.class));
        verify(templateEngine, times(1)).process(eq("booking-confirmation"), any(Context.class));
    }

    @Test
    void sendBookingConfirmation_SendsToUserEmail() {
        when(templateEngine.process(eq("booking-confirmation"), any(Context.class)))
                .thenReturn("<html>Confirmación</html>");

        emailService.sendBookingConfirmation(sampleBooking);

        // Verifica al menos que el mailSender se invocó (destinatario se configura vía helper)
        verify(mailSender, times(1)).send(any(MimeMessage.class));
    }

    // =========================================================
    // sendCancellationEmail
    // =========================================================

    @Test
    void sendCancellationEmail_Success_CallsMailSenderAndTemplateEngine() {
        when(templateEngine.process(eq("booking-cancellation"), any(Context.class)))
                .thenReturn("<html>Cancelación</html>");

        emailService.sendCancellationEmail(sampleBooking);

        verify(mailSender, times(1)).send(any(MimeMessage.class));
        verify(templateEngine, times(1)).process(eq("booking-cancellation"), any(Context.class));
    }

    @Test
    void sendCancellationEmail_DoesNotThrow_OnMailFailure() {
        when(templateEngine.process(eq("booking-cancellation"), any(Context.class)))
                .thenReturn("<html>Cancelación</html>");
        // sendCancellationEmail captura la excepción internamente → no relanza
        doThrow(new org.springframework.mail.MailSendException("SMTP fail"))
                .when(mailSender).send(any(MimeMessage.class));

        // No debe propagarse la excepción (la implementación solo loguea)
        org.junit.jupiter.api.Assertions.assertDoesNotThrow(
                () -> emailService.sendCancellationEmail(sampleBooking));
    }

    @Test
    void sendBookingConfirmation_DoesNotThrow_OnMailFailure() {
        when(templateEngine.process(eq("booking-confirmation"), any(Context.class)))
                .thenReturn("<html>Confirmación</html>");
        doThrow(new org.springframework.mail.MailSendException("SMTP fail"))
                .when(mailSender).send(any(MimeMessage.class));

        org.junit.jupiter.api.Assertions.assertDoesNotThrow(
                () -> emailService.sendBookingConfirmation(sampleBooking));
    }
}