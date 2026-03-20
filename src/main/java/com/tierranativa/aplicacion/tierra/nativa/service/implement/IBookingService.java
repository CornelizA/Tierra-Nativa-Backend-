package com.tierranativa.aplicacion.tierra.nativa.service.implement;

import com.tierranativa.aplicacion.tierra.nativa.dto.BookingRequestDTO;
import com.tierranativa.aplicacion.tierra.nativa.dto.BookingResponseDTO;
import com.tierranativa.aplicacion.tierra.nativa.entity.Booking;
import com.tierranativa.aplicacion.tierra.nativa.entity.PackageTravel;
import com.tierranativa.aplicacion.tierra.nativa.entity.User;
import com.tierranativa.aplicacion.tierra.nativa.exception.ResourceNotFoundException;
import com.tierranativa.aplicacion.tierra.nativa.repository.BookingRepository;
import com.tierranativa.aplicacion.tierra.nativa.repository.PackageTravelRepository;
import com.tierranativa.aplicacion.tierra.nativa.repository.ReviewRepository;
import com.tierranativa.aplicacion.tierra.nativa.repository.UserRepository;
import com.tierranativa.aplicacion.tierra.nativa.service.BookingService;
import com.tierranativa.aplicacion.tierra.nativa.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IBookingService implements BookingService {

    private final BookingRepository bookingRepository;
    private final PackageTravelRepository packageRepository;
    private final EmailService emailService;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public BookingResponseDTO createBooking(BookingRequestDTO request, User user) {

        PackageTravel pkg = packageRepository.findById(request.getPackageId())
                .orElseThrow(() -> new ResourceNotFoundException("El paquete seleccionado no existe."));

        if (user.getPhoneNumber() == null || !user.getPhoneNumber().equals(request.getPhoneNumber())) {
            user.setPhoneNumber(request.getPhoneNumber());
            userRepository.save(user);
        }

        LocalDate expectedEndDate = request.getStartDate().plusDays(pkg.getNumberOfDays() - 1);
        if (!expectedEndDate.equals(request.getEndDate())) {
            throw new IllegalArgumentException(
                    "La duración de la reserva debe ser exactamente " + pkg.getNumberOfDays() +
                    " día(s). La fecha de fin correcta para el " + request.getStartDate() +
                    " es " + expectedEndDate + ".");
        }

        int cuposOcupados = bookingRepository.sumTravelersInOverlap(
                request.getPackageId(),
                request.getStartDate(),
                request.getEndDate()
        );
        int cuposDisponibles = pkg.getCapacity() - cuposOcupados;

        if (request.getTravelerCount() > cuposDisponibles) {
            throw new IllegalStateException(
                    "No hay suficientes cupos disponibles. Solo quedan " + cuposDisponibles +
                    " cupo(s) para las fechas seleccionadas.");
        }

        double totalPrice = pkg.getBasePrice() * request.getTravelerCount();

        Booking booking = Booking.builder()
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .creationDate(LocalDateTime.now())
                .travelerCount(request.getTravelerCount())
                .totalPrice(totalPrice)
                .specialRequests(request.getSpecialRequests())
                .status(request.getStatus() != null ? request.getStatus().toUpperCase() : "CONFIRMED")
                .packageTravel(pkg)
                .user(user)
                .build();

        Booking saved = bookingRepository.save(booking);
        BookingResponseDTO response = BookingResponseDTO.fromEntity(saved);
        emailService.sendBookingConfirmation(response);
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponseDTO> getBookingsByUserId(Long userId) {
        return bookingRepository.findByUserIdOrderByCreationDateDesc(userId).stream()
                .map(b -> {
                    boolean reviewed = reviewRepository.existsByUserIdAndPackageTravelId(
                            userId, b.getPackageTravel().getId());
                    return BookingResponseDTO.fromEntity(b, reviewed);
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public BookingResponseDTO getBookingById(Long bookingId, User user) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("La reserva con ID " + bookingId + " no existe."));

        boolean isOwner = booking.getUser().getId().equals(user.getId());
        boolean isAdmin = user.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ADMIN"));

        if (!isOwner && !isAdmin) {
            throw new IllegalStateException("No tienes permisos para ver esta reserva.");
        }

        return BookingResponseDTO.fromEntity(booking);
    }

    @Override
    @Transactional
    public BookingResponseDTO cancelBooking(Long bookingId, User user) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("La reserva con ID " + bookingId + " no existe."));

        boolean isOwner = booking.getUser().getId().equals(user.getId());
        boolean isAdmin = user.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ADMIN"));

        if (!isOwner && !isAdmin) {
            throw new IllegalStateException("No tienes permisos para cancelar esta reserva.");
        }

        if ("CANCELLED".equalsIgnoreCase(booking.getStatus())) {
            throw new IllegalStateException("La reserva ya se encuentra cancelada.");
        }

        LocalDate limiteCancelacion = booking.getStartDate().minusDays(15);
        if (LocalDate.now().isAfter(limiteCancelacion)) {
            throw new IllegalStateException("No es posible cancelar: el plazo de 15 días previos ha expirado.");
        }

        booking.setStatus("CANCELLED");
        Booking saved = bookingRepository.save(booking);
        return BookingResponseDTO.fromEntity(saved);
    }
}
