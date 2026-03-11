package com.tierranativa.aplicacion.tierra.nativa.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tierranativa.aplicacion.tierra.nativa.dto.AuthenticationRequest;
import com.tierranativa.aplicacion.tierra.nativa.dto.AuthenticationResponse;
import com.tierranativa.aplicacion.tierra.nativa.dto.UserRegistrationRequestDTO;
import com.tierranativa.aplicacion.tierra.nativa.exception.GlobalException;
import com.tierranativa.aplicacion.tierra.nativa.service.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    private MockMvc mockMvc;
    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private AuthController authController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController)
                .setControllerAdvice(new GlobalException())
                .build();
    }

    @Test
    void register_Success() throws Exception {
        UserRegistrationRequestDTO request = new UserRegistrationRequestDTO();
        request.setEmail("test@mail.com");
        request.setFirstName("Juan");
        request.setLastName("Perez");
        request.setPassword("123456");

        AuthenticationResponse response = AuthenticationResponse.builder()
                .email("test@mail.com")
                .firstName("Juan")
                .build();

        when(authenticationService.register(any(UserRegistrationRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("test@mail.com"));
    }

    @Test
    void login_Success() throws Exception {
        AuthenticationRequest request = new AuthenticationRequest("test@mail.com", "123456");
        AuthenticationResponse response = AuthenticationResponse.builder()
                .token("jwt-token-fake")
                .email("test@mail.com")
                .build();

        when(authenticationService.login(any(AuthenticationRequest.class))).thenReturn(response);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-token-fake"));
    }


    @Test
    void resendEmail_Success() throws Exception {
        Map<String, String> request = Map.of("email", "test@example.com");
        doNothing().when(authenticationService).resendWelcomeEmail("test@example.com");

        mockMvc.perform(post("/auth/resend-email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Correo de confirmación reenviado exitosamente."));
    }

    @Test
    void resendEmail_BadRequest_WhenEmailMissing() throws Exception {
        Map<String, String> request = Map.of();

        doThrow(new RuntimeException("El email es requerido"))
                .when(authenticationService).resendWelcomeEmail(null);

        mockMvc.perform(post("/auth/resend-email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("El correo electrónico es obligatorio."));
    }

    @Test
    void verifyEmail_Success() throws Exception {
        String token = "valid-token";
        doNothing().when(authenticationService).activateAccount(token);

        mockMvc.perform(get("/auth/verify-email").param("token", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("¡Cuenta activada! Ya podés iniciar sesión."));
    }

    @Test
    void verifyEmail_TokenExpiredOrInvalid() throws Exception {
        String token = "invalid-token";
        doThrow(new RuntimeException("Token de activación no válido o expirado"))
                .when(authenticationService).activateAccount(token);

        mockMvc.perform(get("/auth/verify-email").param("token", token))
                .andExpect(status().isGone())
                .andExpect(jsonPath("$.error").value("Token de activación no válido o expirado"));
    }

    @Test
    void adminTest_Success() throws Exception {
        mockMvc.perform(get("/auth/admin/test"))
                .andExpect(status().isOk())
                .andExpect(content().string("Acceso de ADMINISTRADOR OK"));
    }
}