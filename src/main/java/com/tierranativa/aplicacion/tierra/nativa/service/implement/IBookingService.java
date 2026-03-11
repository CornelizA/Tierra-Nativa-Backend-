package com.tierranativa.aplicacion.tierra.nativa.service.implement;

import com.tierranativa.aplicacion.tierra.nativa.dto.BookingRequestDTO;
import com.tierranativa.aplicacion.tierra.nativa.dto.BookingResponseDTO;
import com.tierranativa.aplicacion.tierra.nativa.entity.Booking;
import com.tierranativa.aplicacion.tierra.nativa.entity.PackageTravel;
import com.tierranativa.aplicacion.tierra.nativa.entity.User;
import com.tierranativa.aplicacion.tierra.nativa.exception.ResourceNotFoundException;
import com.tierranativa.aplicacion.tierra.nativa.repository.BookingRepository;
import com.tierranativa.aplicacion.tierra.nativa.repository.PackageTravelRepository;
import com.tierranativa.aplicacion.tierra.nativa.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IBookingService implements BookingService {

    private final BookingRepository bookingRepository;
    private final PackageTravelRepository packageRepository;

    @Override
    @Transactional
    public BookingResponseDTO createBooking(BookingRequestDTO request, User user) {
        PackageTravel pkg = packageRepository.findById(request.getPackageId())
                .orElseThrow(() -> new ResourceNotFoundException("Paquete no encontrado con ID: " + request.getPackageId()));

        Booking booking = Booking.builder()
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .status(request.getStatus() != null ? request.getStatus().toUpperCase() : "CONFIRMED")
                .packageTravel(pkg)
                .user(user)
                .build();

        Booking saved = bookingRepository.save(booking);
        return BookingResponseDTO.fromEntity(saved);
    }


    @Override
    @Transactional(readOnly = true)
    public List<BookingResponseDTO> getBookingsByUserId(Long userId) {
        return bookingRepository.findByUserId(userId).stream()
                .map(BookingResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }
}
