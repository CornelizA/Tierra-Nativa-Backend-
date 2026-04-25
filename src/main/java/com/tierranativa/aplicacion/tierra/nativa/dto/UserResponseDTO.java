package com.tierranativa.aplicacion.tierra.nativa.dto;

import com.tierranativa.aplicacion.tierra.nativa.entity.RoleLogin;
import com.tierranativa.aplicacion.tierra.nativa.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private RoleLogin role;
    private String phone;

    public static UserResponseDTO fromEntity(User user) {
        if (user == null) return null;
        return UserResponseDTO.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .role(user.getRole())
                .phone(user.getPhoneNumber())
                .build();
    }
}
