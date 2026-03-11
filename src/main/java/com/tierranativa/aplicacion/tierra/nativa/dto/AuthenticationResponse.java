package com.tierranativa.aplicacion.tierra.nativa.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {

    private String token;
    private String email;
    private String role;
    private String firstName;
    private String lastName;
}
