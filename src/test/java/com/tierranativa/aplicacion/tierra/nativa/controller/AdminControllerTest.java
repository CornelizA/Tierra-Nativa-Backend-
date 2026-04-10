package com.tierranativa.aplicacion.tierra.nativa.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tierranativa.aplicacion.tierra.nativa.dto.BookingResponseDTO;
import com.tierranativa.aplicacion.tierra.nativa.dto.UserRoleUpdateRequestDTO;
import com.tierranativa.aplicacion.tierra.nativa.entity.RoleLogin;
import com.tierranativa.aplicacion.tierra.nativa.entity.User;
import com.tierranativa.aplicacion.tierra.nativa.service.BookingService;
import com.tierranativa.aplicacion.tierra.nativa.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AdminControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @Mock
    private BookingService bookingService;

    @InjectMocks
    private AdminController adminController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(adminController).build();
    }

    // =========================================================
    // PUT /admin/role
    // =========================================================

    @Test
    void updateRole_Success() throws Exception {
        UserRoleUpdateRequestDTO request = new UserRoleUpdateRequestDTO("user@test.com", RoleLogin.ADMIN);
        User updatedUser = User.builder()
                .email("user@test.com")
                .role(RoleLogin.ADMIN)
                .firstName("Juan")
                .lastName("Perez")
                .enabled(true)
                .build();

        when(userService.updateRole(any(UserRoleUpdateRequestDTO.class))).thenReturn(updatedUser);

        mockMvc.perform(put("/admin/role")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.role").value("ADMIN"))
                .andExpect(jsonPath("$.email").value("user@test.com"));
    }

    // =========================================================
    // GET /admin
    // =========================================================

    @Test
    void getAllUsers_Success() throws Exception {
        User user1 = User.builder().email("admin@test.com").role(RoleLogin.ADMIN).build();
        User user2 = User.builder().email("user@test.com").role(RoleLogin.USER).build();

        when(userService.findAll()).thenReturn(List.of(user1, user2));

        mockMvc.perform(get("/admin").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].role").value("ADMIN"))
                .andExpect(jsonPath("$[1].role").value("USER"));
    }

    // =========================================================
    // GET /admin/bookings
    // =========================================================

    @Test
    void getAllBookings_Success_ReturnsAllBookings() throws Exception {
        BookingResponseDTO booking = BookingResponseDTO.builder()
                .id(1L)
                .packageName("Glaciar Perito Moreno")
                .packageDestination("El Calafate")
                .startDate(LocalDate.now().plusDays(30))
                .endDate(LocalDate.now().plusDays(33))
                .status("CONFIRMED")
                .travelerCount(2)
                .totalPrice(200000.0)
                .userEmail("viajero@test.com")
                .userFirstName("Juan")
                .userLastName("Perez")
                .build();

        when(bookingService.getAllBookingsForAdmin()).thenReturn(List.of(booking));

        mockMvc.perform(get("/admin/bookings").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].packageName").value("Glaciar Perito Moreno"))
                .andExpect(jsonPath("$[0].status").value("CONFIRMED"));
    }

    @Test
    void getAllBookings_ReturnsEmptyList_WhenNoBookings() throws Exception {
        when(bookingService.getAllBookingsForAdmin()).thenReturn(List.of());

        mockMvc.perform(get("/admin/bookings").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    // =========================================================
    // PATCH /admin/bookings/{id}/contacted
    // =========================================================

    @Test
    void updateContactedStatus_True_Success() throws Exception {
        BookingResponseDTO updated = BookingResponseDTO.builder()
                .id(1L)
                .packageName("Glaciar Perito Moreno")
                .status("CONFIRMED")
                .isContacted(true)
                .build();

        when(bookingService.updateContactedStatus(eq(1L), eq(true))).thenReturn(updated);

        mockMvc.perform(patch("/admin/bookings/1/contacted")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("contacted", true))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.contacted").value(true));
    }

    @Test
    void updateContactedStatus_False_Success() throws Exception {
        BookingResponseDTO updated = BookingResponseDTO.builder()
                .id(1L)
                .packageName("Glaciar Perito Moreno")
                .status("CONFIRMED")
                .isContacted(false)
                .build();

        when(bookingService.updateContactedStatus(eq(1L), eq(false))).thenReturn(updated);

        mockMvc.perform(patch("/admin/bookings/1/contacted")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("contacted", false))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contacted").value(false));
    }
}