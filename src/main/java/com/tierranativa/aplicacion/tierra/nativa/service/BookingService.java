package com.tierranativa.aplicacion.tierra.nativa.service;

import com.tierranativa.aplicacion.tierra.nativa.dto.BookingRequestDTO;
import com.tierranativa.aplicacion.tierra.nativa.dto.BookingResponseDTO;
import com.tierranativa.aplicacion.tierra.nativa.entity.User;

import java.util.List;

public interface BookingService {

    BookingResponseDTO createBooking(BookingRequestDTO request, User user);

    List<BookingResponseDTO> getBookingsByUserId(Long userId);
}