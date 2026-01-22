package com.tierranativa.aplicacion.tierra.nativa.service;

import com.tierranativa.aplicacion.tierra.nativa.dto.UserRoleUpdateRequestDTO;
import com.tierranativa.aplicacion.tierra.nativa.entity.RoleLogin;
import com.tierranativa.aplicacion.tierra.nativa.entity.User;
import com.tierranativa.aplicacion.tierra.nativa.exception.ResourceNotFoundException;
import com.tierranativa.aplicacion.tierra.nativa.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User mockUser;

    @BeforeEach
    void setUp() {
        mockUser = User.builder()
                .id(1L)
                .firstName("Admin")
                .lastName("Test")
                .email("tierranativa.dev@gmail.com")
                .role(RoleLogin.USER)
                .enabled(true)
                .build();
    }

    @Test
    void updateRole_Success() {
        UserRoleUpdateRequestDTO request = UserRoleUpdateRequestDTO.builder()
                .email("tierranativa.dev@gmail.com")
                .newRole(RoleLogin.ADMIN)
                .build();

        when(userRepository.findByEmail("tierranativa.dev@gmail.com")).thenReturn(Optional.of(mockUser));
        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        User result = userService.updateRole(request);

        assertThat(result.getRole()).isEqualTo(RoleLogin.ADMIN);
        verify(userRepository, times(1)).save(mockUser);
    }

    @Test
    void updateRole_UserNotFound_ThrowsException() {
        UserRoleUpdateRequestDTO request = UserRoleUpdateRequestDTO.builder()
                .email("noexiste@mail.com")
                .newRole(RoleLogin.ADMIN)
                .build();

        when(userRepository.findByEmail("noexiste@mail.com")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.updateRole(request));
        verify(userRepository, never()).save(any());
    }

    @Test
    void findAll_Success() {
        when(userRepository.findAll()).thenReturn(List.of(mockUser));

        List<User> result = userService.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getEmail()).isEqualTo("tierranativa.dev@gmail.com");
        verify(userRepository, times(1)).findAll();
    }
}