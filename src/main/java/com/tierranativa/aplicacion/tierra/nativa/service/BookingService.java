package com.tierranativa.aplicacion.tierra.nativa.service;

import com.tierranativa.aplicacion.tierra.nativa.dto.BookingRequestDTO;
import com.tierranativa.aplicacion.tierra.nativa.dto.BookingResponseDTO;
import com.tierranativa.aplicacion.tierra.nativa.entity.User;

import java.time.LocalDate;
import java.util.List;

public interface BookingService {

    BookingResponseDTO createBooking(BookingRequestDTO request, User user);

    List<BookingResponseDTO> getBookingsByUserId(Long userId);

    BookingResponseDTO getBookingById(Long bookingId, User user);

    BookingResponseDTO cancelBooking(Long bookingId, User user);

    List<BookingResponseDTO> getAllBookingsForAdmin();

    BookingResponseDTO updateContactedStatus(Long bookingId, boolean contacted);

    int getAvailableSpots(Long packageId, LocalDate startDate, LocalDate endDate);
}