package com.tierranativa.aplicacion.tierra.nativa.controller;

import com.tierranativa.aplicacion.tierra.nativa.dto.AuthenticationRequest;
import com.tierranativa.aplicacion.tierra.nativa.dto.UserRegistrationRequestDTO;
import com.tierranativa.aplicacion.tierra.nativa.dto.AuthenticationResponse;
import com.tierranativa.aplicacion.tierra.nativa.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@Valid @RequestBody UserRegistrationRequestDTO request) {
        AuthenticationResponse response = authenticationService.register(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@Valid @RequestBody AuthenticationRequest request) {
        AuthenticationResponse response = authenticationService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/resend-email")
    public ResponseEntity<Map<String, String>> resendEmail(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        if (email == null || email.trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "El correo electrónico es obligatorio."));
        }
        try {
            authenticationService.resendWelcomeEmail(email);
            return ResponseEntity.ok(Map.of("message", "Correo de confirmación reenviado exitosamente."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "No se pudo procesar el reenvío: " + e.getMessage()));
        }
    }

    @GetMapping("/verify-email")
    public ResponseEntity<Map<String, String>> verifyEmail(@RequestParam(name = "token") String token) {
        if (token == null || token.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Token es obligatorio."));
        }
        try {
            authenticationService.activateAccount(token);
            return ResponseEntity.ok(Map.of("message", "¡Cuenta activada! Ya podés iniciar sesión."));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.GONE)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Ocurrió un error inesperado."));
        }
    }

    @GetMapping("/admin/test")
    public ResponseEntity<String> adminTest() {
        return ResponseEntity.ok("Acceso de ADMINISTRADOR OK");
    }
}
