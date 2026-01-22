package com.tierranativa.aplicacion.tierra.nativa.dto;

import com.tierranativa.aplicacion.tierra.nativa.entity.RoleLogin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRoleUpdateRequestDTO {

    @NotNull(message = "El email no puede ser nulo.")
    @Email(message = "Debe ser un formato de email válido.")
    private String email;

    @NotNull(message = "El rol no puede ser nulo.")
    private RoleLogin newRole;
}
