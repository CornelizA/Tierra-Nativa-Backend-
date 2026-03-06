package com.tierranativa.aplicacion.tierra.nativa.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tierranativa.aplicacion.tierra.nativa.dto.BookingRequestDTO;
import com.tierranativa.aplicacion.tierra.nativa.dto.BookingResponseDTO;
import com.tierranativa.aplicacion.tierra.nativa.entity.User;
import com.tierranativa.aplicacion.tierra.nativa.entity.RoleLogin;
import com.tierranativa.aplicacion.tierra.nativa.service.BookingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private BookingService bookingService;

    private BookingRequestDTO validRequest;
    private BookingResponseDTO mockResponse;
    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .email("viajero@test.com")
                .firstName("Juan")
                .lastName("Perez")
                .role(RoleLogin.USER)
                .enabled(true)
                .build();

        validRequest = new BookingRequestDTO();
        validRequest.setPackageId(1L);
        validRequest.setStartDate(LocalDate.now().plusDays(5));
        validRequest.setEndDate(LocalDate.now().plusDays(10));
        validRequest.setStatus("CONFIRMED");

        mockResponse = BookingResponseDTO.builder()
                .id(100L)
                .packageName("Glaciar Perito Moreno")
                .startDate(validRequest.getStartDate())
                .endDate(validRequest.getEndDate())
                .status("CONFIRMED")
                .build();
    }

    @Test
    void createBooking_Success() throws Exception {
        Mockito.when(bookingService.createBooking(any(), any()))
                .thenReturn(mockResponse);

        mockMvc.perform(post("/bookings")
                        .with(user(testUser))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(100))
                .andExpect(jsonPath("$.packageName").value("Glaciar Perito Moreno"));
    }

    @Test
    void getMyBookings_Success() throws Exception {
        Mockito.when(bookingService.getBookingsByUserId(1L))
                .thenReturn(List.of(mockResponse));

        mockMvc.perform(get("/bookings/my-bookings")
                        .with(user(testUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].packageName").value("Glaciar Perito Moreno"));
    }

    @Test
    void createBooking_NoAvailability_ReturnsError() throws Exception {
        Mockito.when(bookingService.createBooking(any(), any()))
                .thenThrow(new RuntimeException("No hay disponibilidad para las fechas seleccionadas"));

        mockMvc.perform(post("/bookings")
                        .with(user(testUser))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("No hay disponibilidad para las fechas seleccionadas"));
    }

    @Test
    void createBooking_Unauthorized() throws Exception {
        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isUnauthorized());
    }
}