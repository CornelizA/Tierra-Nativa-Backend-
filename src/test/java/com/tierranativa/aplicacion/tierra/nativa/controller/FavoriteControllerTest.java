package com.tierranativa.aplicacion.tierra.nativa.controller;

import com.tierranativa.aplicacion.tierra.nativa.dto.PackageTravelRequestDTO;
import com.tierranativa.aplicacion.tierra.nativa.entity.RoleLogin;
import com.tierranativa.aplicacion.tierra.nativa.entity.User;
import com.tierranativa.aplicacion.tierra.nativa.service.FavoriteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class FavoriteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FavoriteService favoriteService;

    private PackageTravelRequestDTO samplePackage;
    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .email("test@tierranativa.com")
                .firstName("Usuario")
                .lastName("Prueba")
                .role(RoleLogin.USER)
                .enabled(true)
                .build();

        samplePackage = PackageTravelRequestDTO.builder()
                .id(1L)
                .name("Glaciar Perito Moreno")
                .destination("El Calafate")
                .basePrice(500000.0)
                .isFavorite(true)
                .build();
    }

    @Test
    void shouldToggleFavoriteToTrue() throws Exception {
        Mockito.when(favoriteService.toggleFavorite(anyLong(), anyLong())).thenReturn(true);

        mockMvc.perform(post("/favorites/toggle/1")
                        .with(user(testUser))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isFavorite").value(true));
    }

    @Test
    void shouldToggleFavoriteToFalse() throws Exception {
        Mockito.when(favoriteService.toggleFavorite(anyLong(), anyLong())).thenReturn(false);

        mockMvc.perform(post("/favorites/toggle/1")
                        .with(user(testUser))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isFavorite").value(false));
    }

    @Test
    void shouldReturnUserFavoritesList() throws Exception {
        Mockito.when(favoriteService.getFavoritesByUser(anyLong()))
                .thenReturn(List.of(samplePackage));

        mockMvc.perform(get("/favorites")
                        .with(user(testUser))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").value("Glaciar Perito Moreno"));
    }

    @Test
    void shouldReturnUnauthorizedWhenNotLoggedIn() throws Exception {
        mockMvc.perform(get("/favorites")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}