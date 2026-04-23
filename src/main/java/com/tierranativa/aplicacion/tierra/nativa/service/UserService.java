package com.tierranativa.aplicacion.tierra.nativa.service;

import com.tierranativa.aplicacion.tierra.nativa.dto.UserResponseDTO;
import com.tierranativa.aplicacion.tierra.nativa.dto.UserRoleUpdateRequestDTO;
import com.tierranativa.aplicacion.tierra.nativa.entity.User;
import com.tierranativa.aplicacion.tierra.nativa.exception.ResourceNotFoundException;
import com.tierranativa.aplicacion.tierra.nativa.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public UserResponseDTO updateRole(UserRoleUpdateRequestDTO request) {
        String emailToSearch = request.getEmail().trim().toLowerCase();

        User user = userRepository.findByEmail(emailToSearch)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con email: " + emailToSearch));

        user.setRole(request.getNewRole());
        User saved = userRepository.save(user);
        return UserResponseDTO.fromEntity(saved);
    }

    public List<UserResponseDTO> findAll() {
        return userRepository.findAll().stream()
                .map(UserResponseDTO::fromEntity)
                .toList();
    }
}