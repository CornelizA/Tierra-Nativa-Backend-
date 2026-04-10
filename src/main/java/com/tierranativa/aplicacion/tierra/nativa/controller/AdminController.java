package com.tierranativa.aplicacion.tierra.nativa.controller;

import com.tierranativa.aplicacion.tierra.nativa.dto.BookingResponseDTO;
import com.tierranativa.aplicacion.tierra.nativa.dto.UserRoleUpdateRequestDTO;
import com.tierranativa.aplicacion.tierra.nativa.entity.User;
import com.tierranativa.aplicacion.tierra.nativa.service.BookingService;
import com.tierranativa.aplicacion.tierra.nativa.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;
    private final BookingService bookingService;

    @PutMapping("/role")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<User> updateRole(@Valid @RequestBody UserRoleUpdateRequestDTO request) {
        return ResponseEntity.ok(userService.updateRole(request));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/bookings")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<BookingResponseDTO>> getAllBookings() {
        return ResponseEntity.ok(bookingService.getAllBookingsForAdmin());
    }

    @PatchMapping("/bookings/{id}/contacted")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<BookingResponseDTO> updateContactedStatus(@PathVariable Long id, @RequestBody Map<String, Boolean> body) {
        boolean contacted = Boolean.TRUE.equals(body.get("contacted"));
        return ResponseEntity.ok(bookingService.updateContactedStatus(id, contacted));
    }
}