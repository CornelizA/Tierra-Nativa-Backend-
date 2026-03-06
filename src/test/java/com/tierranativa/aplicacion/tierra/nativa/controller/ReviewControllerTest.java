package com.tierranativa.aplicacion.tierra.nativa.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tierranativa.aplicacion.tierra.nativa.dto.ReviewDTO;
import com.tierranativa.aplicacion.tierra.nativa.dto.ReviewRequestDTO;
import com.tierranativa.aplicacion.tierra.nativa.entity.RoleLogin;
import com.tierranativa.aplicacion.tierra.nativa.entity.User;
import com.tierranativa.aplicacion.tierra.nativa.service.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ReviewService reviewService;

    private User testUser;
    private ReviewRequestDTO validRequest;
    private ReviewDTO mockResponse;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .email("viajero@tierranativa.com")
                .firstName("Juan")
                .lastName("Perez")
                .role(RoleLogin.USER)
                .enabled(true)
                .build();

        validRequest = ReviewRequestDTO.builder()
                .packageId(5L)
                .score(5)
                .comment("Una experiencia increíble, muy bien organizado.")
                .build();

        mockResponse = ReviewDTO.builder()
                .id(100L)
                .score(5)
                .comment("Una experiencia increíble, muy bien organizado.")
                .userName("Juan Perez")
                .date(LocalDateTime.now())
                .build();
    }

    @Test
    void createReview_Success() throws Exception {
        Mockito.when(reviewService.addReview(argThat(r -> r.getComment().length() >= 10), any()))
                .thenReturn(mockResponse);

        mockMvc.perform(post("/reviews")
                        .with(user(testUser))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(100));
    }

    @Test
    void updateReview_Success() throws Exception {
        Mockito.when(reviewService.updateReview(any(), any(), any()))
                .thenReturn(mockResponse);

        mockMvc.perform(put("/reviews/100")
                        .with(user(testUser))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isOk());
    }

    @Test
    void deleteReview_Success() throws Exception {
        Mockito.doNothing().when(reviewService).deleteReview(any(), any());

        mockMvc.perform(delete("/reviews/100")
                        .with(user(testUser)))
                .andExpect(status().isNoContent());
    }

    @Test
    void createReview_InvalidRequest_ReturnsBadRequest() throws Exception {
        validRequest.setComment("Corto");

        Mockito.when(reviewService.addReview(argThat(r -> r.getComment().equals("Corto")), any()))
                .thenThrow(new RuntimeException("El correo electrónico o comentario es obligatorio o inválido."));

        mockMvc.perform(post("/reviews")
                        .with(user(testUser))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void getReviewsByPackage_Success() throws Exception {
        Mockito.when(reviewService.getReviewsByPackage(any()))
                .thenReturn(List.of(mockResponse));

        mockMvc.perform(get("/reviews/package/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
}