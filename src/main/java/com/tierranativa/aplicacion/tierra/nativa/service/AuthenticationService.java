package com.tierranativa.aplicacion.tierra.nativa.service;

import com.tierranativa.aplicacion.tierra.nativa.dto.AuthenticationRequest;
import com.tierranativa.aplicacion.tierra.nativa.dto.UserRegistrationRequestDTO;
import com.tierranativa.aplicacion.tierra.nativa.dto.AuthenticationResponse;
import com.tierranativa.aplicacion.tierra.nativa.entity.RoleLogin;
import com.tierranativa.aplicacion.tierra.nativa.entity.User;
import com.tierranativa.aplicacion.tierra.nativa.exception.ResourceAlreadyExistsException;
import com.tierranativa.aplicacion.tierra.nativa.exception.ResourceNotFoundException;
import com.tierranativa.aplicacion.tierra.nativa.repository.UserRepository;
import com.tierranativa.aplicacion.tierra.nativa.security.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;

    @Transactional
    public AuthenticationResponse register(UserRegistrationRequestDTO request) {
        String normalizedEmail = request.getEmail().toLowerCase().trim();

        if (userRepository.existsByEmail(normalizedEmail)) {
            logger.warn("Intento de registro con correo ya existente: {}", normalizedEmail);
            throw new ResourceAlreadyExistsException("El correo electrónico ya está en uso.");
        }

        RoleLogin assignedRole = normalizedEmail.equalsIgnoreCase("tierranativa.dev@gmail.com")
                ? RoleLogin.ADMIN
                : RoleLogin.USER;

        String token = UUID.randomUUID().toString();

        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(normalizedEmail)
                .password(passwordEncoder.encode(request.getPassword()))
                .role(assignedRole)
                .enabled(false)
                .activationToken(token)
                .build();

        userRepository.save(user);
        logger.info("Nuevo usuario registrado: {} con rol {}", normalizedEmail, assignedRole);

        emailService.sendWelcomeEmail(user.getEmail(), user.getFirstName(), user.getLastName(), token);

        return AuthenticationResponse.builder()
                .email(user.getEmail())
                .role(user.getRole().name())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }

    @Transactional(readOnly = true)
    public AuthenticationResponse login(@Valid AuthenticationRequest request) {
        String normalizedEmail = request.getEmail().toLowerCase().trim();

        var user = userRepository.findByEmail(normalizedEmail)
                .orElseThrow(() -> {
                    logger.error("Fallo de login: Usuario {} no encontrado", normalizedEmail);
                    return new ResourceNotFoundException("Usuario no encontrado");
                });
        if (!user.isEnabled()) {
            logger.warn("Intento de inicio de sesión con cuenta no activada: {}", normalizedEmail);
            throw new org.springframework.security.authentication.DisabledException(
                    "Debes activar tu cuenta por correo electrónico antes de iniciar sesión."
            );
        }
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(normalizedEmail, request.getPassword())
            );
        } catch (BadCredentialsException e) {
            logger.error("Credenciales incorrectas para el usuario: {}", normalizedEmail);
            throw new BadCredentialsException("Credenciales incorrectas.");
        }

        var jwtToken = jwtService.generateToken(user);
        logger.info("Usuario autenticado exitosamente: {}", normalizedEmail);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .email(user.getEmail())
                .role(user.getRole().name())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }

    @Transactional
    public void activateAccount(String token) {
        User user = userRepository.findByActivationToken(token)
                .orElseGet(() -> {
                    logger.error("Intento de activación con token inválido o expirado");
                    throw new ResourceNotFoundException("El token de activación es inválido o ya ha sido procesado.");
                });
        user.setEnabled(true);
        user.setActivationToken(null);
        userRepository.save(user);
        logger.info("✅ Cuenta activada exitosamente para: {}", user.getEmail());
    }

    @Transactional
    public void resendWelcomeEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new RuntimeException("El email es requerido para el reenvío.");
        }
        String normalizedEmail = email.toLowerCase().trim();

        User user = userRepository.findByEmail(normalizedEmail)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró una cuenta asociada a: " + normalizedEmail));

        if (user.isEnabled()) {
            logger.info("Solicitud de reenvío para cuenta ya activa: {}", normalizedEmail);
            throw new IllegalStateException("La cuenta ya se encuentra activa.");
        }
        if (user.getActivationToken() == null || user.getActivationToken().isBlank()) {
            user.setActivationToken(UUID.randomUUID().toString());
            userRepository.save(user);
        }

        emailService.sendWelcomeEmail(user.getEmail(), user.getFirstName(), user.getLastName(), user.getActivationToken());
        logger.info("Correo de bienvenida reenviado a: {}", normalizedEmail);
    }
}
