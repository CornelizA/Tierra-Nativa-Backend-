package com.tierranativa.aplicacion.tierra.nativa.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tierranativa.aplicacion.tierra.nativa.dto.BookingRequestDTO;
import com.tierranativa.aplicacion.tierra.nativa.dto.BookingResponseDTO;
import com.tierranativa.aplicacion.tierra.nativa.entity.RoleLogin;
import com.tierranativa.aplicacion.tierra.nativa.entity.User;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
        validRequest.setStartDate(LocalDate.now().plusDays(30));
        validRequest.setEndDate(LocalDate.now().plusDays(33));
        validRequest.setTravelerCount(2);
        validRequest.setPhoneNumber("1234567890");
        validRequest.setStatus("CONFIRMED");

        mockResponse = BookingResponseDTO.builder()
                .id(100L)
                .packageName("Glaciar Perito Moreno")
                .packageDestination("El Calafate")
                .startDate(validRequest.getStartDate())
                .endDate(validRequest.getEndDate())
                .travelerCount(2)
                .totalPrice(200000.0)
                .status("CONFIRMED")
                .userEmail("viajero@test.com")
                .userFirstName("Juan")
                .userLastName("Perez")
                .build();
    }

    // =========================================================
    // POST /bookings
    // =========================================================

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
                .andExpect(jsonPath("$.packageName").value("Glaciar Perito Moreno"))
                .andExpect(jsonPath("$.totalPrice").value(200000.0));
    }

    @Test
    void createBooking_Unauthorized() throws Exception {
        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void createBooking_NoAvailability_ReturnsInternalServerError() throws Exception {
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
    void createBooking_CapacityExceeded_ReturnsBadRequest() throws Exception {
        Mockito.when(bookingService.createBooking(any(), any()))
                .thenThrow(new IllegalStateException("No hay suficientes cupos disponibles. Solo quedan 1 cupo(s)"));

        mockMvc.perform(post("/bookings")
                        .with(user(testUser))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                // IllegalStateException → GlobalException → 400 BAD REQUEST
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
    }

    // =========================================================
    // GET /bookings/my-bookings
    // =========================================================

    @Test
    void getMyBookings_Success() throws Exception {
        Mockito.when(bookingService.getBookingsByUserId(1L))
                .thenReturn(List.of(mockResponse));

        mockMvc.perform(get("/bookings/my-bookings")
                        .with(user(testUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].packageName").value("Glaciar Perito Moreno"))
                .andExpect(jsonPath("$[0].status").value("CONFIRMED"));
    }

    @Test
    void getMyBookings_ReturnsEmptyList_WhenNoBookings() throws Exception {
        Mockito.when(bookingService.getBookingsByUserId(1L))
                .thenReturn(List.of());

        mockMvc.perform(get("/bookings/my-bookings")
                        .with(user(testUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void getMyBookings_Unauthorized() throws Exception {
        mockMvc.perform(get("/bookings/my-bookings"))
                .andExpect(status().isUnauthorized());
    }

    // =========================================================
    // GET /bookings/{id}
    // =========================================================

    @Test
    void getBookingById_Success() throws Exception {
        Mockito.when(bookingService.getBookingById(eq(100L), any()))
                .thenReturn(mockResponse);

        mockMvc.perform(get("/bookings/100")
                        .with(user(testUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(100))
                .andExpect(jsonPath("$.packageName").value("Glaciar Perito Moreno"));
    }

    @Test
    void getBookingById_Unauthorized() throws Exception {
        mockMvc.perform(get("/bookings/100"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getBookingById_NoPermission_ReturnsBadRequest() throws Exception {
        Mockito.when(bookingService.getBookingById(anyLong(), any()))
                .thenThrow(new IllegalStateException("No tienes permisos para ver esta reserva."));

        mockMvc.perform(get("/bookings/100")
                        .with(user(testUser)))
                // IllegalStateException → GlobalException → 400 BAD REQUEST
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("No tienes permisos para ver esta reserva."));
    }

    // =========================================================
    // PATCH /bookings/{id}/cancel
    // =========================================================

    @Test
    void cancelBooking_Success() throws Exception {
        BookingResponseDTO cancelled = BookingResponseDTO.builder()
                .id(100L)
                .packageName("Glaciar Perito Moreno")
                .status("CANCELLED")
                .build();

        Mockito.when(bookingService.cancelBooking(eq(100L), any()))
                .thenReturn(cancelled);

        mockMvc.perform(patch("/bookings/100/cancel")
                        .with(user(testUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(100))
                .andExpect(jsonPath("$.status").value("CANCELLED"));
    }

    @Test
    void cancelBooking_TooLate_ReturnsBadRequest() throws Exception {
        Mockito.when(bookingService.cancelBooking(anyLong(), any()))
                .thenThrow(new IllegalStateException("No es posible cancelar: el plazo de 15 días previos ha expirado."));

        mockMvc.perform(patch("/bookings/100/cancel")
                        .with(user(testUser)))
                // IllegalStateException → GlobalException → 400 BAD REQUEST
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void cancelBooking_Unauthorized() throws Exception {
        mockMvc.perform(patch("/bookings/100/cancel"))
                .andExpect(status().isUnauthorized());
    }

    // =========================================================
    // GET /bookings/{id}/available-capacity
    // =========================================================

    @Test
    void getAvailableCapacity_Success() throws Exception {
        Mockito.when(bookingService.getAvailableSpots(anyLong(), any(), any()))
                .thenReturn(7);

        // /bookings/** requiere autenticación según SecurityConfig
        mockMvc.perform(get("/bookings/10/available-capacity")
                        .with(user(testUser))
                        .param("startDate", LocalDate.now().plusDays(30).toString())
                        .param("endDate", LocalDate.now().plusDays(33).toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.availableSpots").value(7));
    }

    @Test
    void getAvailableCapacity_ReturnsZero_WhenFull() throws Exception {
        Mockito.when(bookingService.getAvailableSpots(anyLong(), any(), any()))
                .thenReturn(0);

        mockMvc.perform(get("/bookings/10/available-capacity")
                        .with(user(testUser))
                        .param("startDate", LocalDate.now().plusDays(30).toString())
                        .param("endDate", LocalDate.now().plusDays(33).toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.availableSpots").value(0));
    }
}