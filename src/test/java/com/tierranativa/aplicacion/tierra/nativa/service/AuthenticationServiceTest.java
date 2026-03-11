package com.tierranativa.aplicacion.tierra.nativa.service;

import com.tierranativa.aplicacion.tierra.nativa.dto.*;
import com.tierranativa.aplicacion.tierra.nativa.entity.RoleLogin;
import com.tierranativa.aplicacion.tierra.nativa.entity.User;
import com.tierranativa.aplicacion.tierra.nativa.repository.UserRepository;
import com.tierranativa.aplicacion.tierra.nativa.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private JwtService jwtService;
    @Mock private AuthenticationManager authenticationManager;
    @Mock private EmailService emailService;

    @InjectMocks
    private AuthenticationService authenticationService;

    private UserRegistrationRequestDTO registrationRequest;
    private User mockUser;

    @BeforeEach
    void setUp() {
        registrationRequest = new UserRegistrationRequestDTO();
        registrationRequest.setEmail("test@example.com");
        registrationRequest.setFirstName("Juan");
        registrationRequest.setLastName("Perez");
        registrationRequest.setPassword("password123");

        mockUser = User.builder()
                .email("test@example.com")
                .firstName("Juan")
                .lastName("Perez")
                .role(RoleLogin.USER)
                .enabled(false)
                .activationToken("token-123")
                .build();
    }

    @Test
    void register_Success() {
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        AuthenticationResponse response = authenticationService.register(registrationRequest);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();
        assertThat(savedUser.isEnabled()).isFalse();
        assertThat(savedUser.getActivationToken()).isNotNull();
        verify(emailService).sendWelcomeEmail(eq("test@example.com"), any(), any(), any());
        assertThat(response.getEmail()).isEqualTo("test@example.com");
    }

    @Test
    void register_AdminRole_WhenSpecialEmail() {
        registrationRequest.setEmail("tierranativa.dev@gmail.com");
        when(userRepository.existsByEmail(anyString())).thenReturn(false);

        authenticationService.register(registrationRequest);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        assertThat(userCaptor.getValue().getRole()).isEqualTo(RoleLogin.ADMIN);
    }

    @Test
    void login_Success() {
        AuthenticationRequest loginRequest = new AuthenticationRequest("test@example.com", "password123");
        mockUser.setEnabled(true);

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(mockUser));
        when(jwtService.generateToken(any())).thenReturn("jwt-token");

        AuthenticationResponse response = authenticationService.login(loginRequest);

        assertThat(response.getToken()).isEqualTo("jwt-token");
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void login_ThrowsException_WhenUserDisabled() {
        AuthenticationRequest loginRequest = new AuthenticationRequest("test@example.com", "password123");
        mockUser.setEnabled(false);

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(mockUser));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> authenticationService.login(loginRequest));
        assertThat(exception.getMessage()).contains("activar tu cuenta");
    }

    @Test
    void activateAccount_Success() {
        String token = "token-123";
        when(userRepository.findByActivationToken(token)).thenReturn(Optional.of(mockUser));

        authenticationService.activateAccount(token);

        assertThat(mockUser.isEnabled()).isTrue();
        assertThat(mockUser.getActivationToken()).isNull();
        verify(userRepository).save(mockUser);
    }

    @Test
    void resendWelcomeEmail_Success() {
        String email = "test@example.com";
        mockUser.setActivationToken(null);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(mockUser));

        authenticationService.resendWelcomeEmail(email);

        verify(userRepository).save(mockUser);
        verify(emailService).sendWelcomeEmail(eq(email), any(), any(), any());
    }

    @Test
    void resendWelcomeEmail_Success_WhenUserDisabled() {
        String email = "test@example.com";
        mockUser.setEnabled(false);
        mockUser.setActivationToken(null);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(mockUser));

        authenticationService.resendWelcomeEmail(email);

        assertThat(mockUser.getActivationToken()).isNotNull();
        verify(userRepository).save(mockUser);
        verify(emailService).sendWelcomeEmail(eq(email), any(), any(), anyString());
    }

    @Test
    void resendWelcomeEmail_ThrowsException_WhenUserAlreadyEnabled() {
        String email = "active@example.com";
        mockUser.setEnabled(true);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(mockUser));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> authenticationService.resendWelcomeEmail(email));

        assertThat(exception.getMessage()).contains("ya se encuentra activa");
        verify(userRepository, never()).save(any());
        verify(emailService, never()).sendWelcomeEmail(any(), any(), any(), any());
    }
}