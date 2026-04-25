package com.tierranativa.aplicacion.tierra.nativa.service.implement;

import com.tierranativa.aplicacion.tierra.nativa.dto.BookingRequestDTO;
import com.tierranativa.aplicacion.tierra.nativa.dto.BookingResponseDTO;
import com.tierranativa.aplicacion.tierra.nativa.entity.Booking;
import com.tierranativa.aplicacion.tierra.nativa.entity.PackageTravel;
import com.tierranativa.aplicacion.tierra.nativa.entity.RoleLogin;
import com.tierranativa.aplicacion.tierra.nativa.entity.User;
import com.tierranativa.aplicacion.tierra.nativa.exception.ResourceNotFoundException;
import com.tierranativa.aplicacion.tierra.nativa.repository.BookingRepository;
import com.tierranativa.aplicacion.tierra.nativa.repository.PackageTravelRepository;
import com.tierranativa.aplicacion.tierra.nativa.repository.ReviewRepository;
import com.tierranativa.aplicacion.tierra.nativa.repository.UserRepository;
import com.tierranativa.aplicacion.tierra.nativa.service.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IBookingServiceTest {

    @Mock private BookingRepository bookingRepository;
    @Mock private PackageTravelRepository packageRepository;
    @Mock private EmailService emailService;
    @Mock private ReviewRepository reviewRepository;
    @Mock private UserRepository userRepository;

    @InjectMocks
    private IBookingService bookingService;

    @Captor
    private ArgumentCaptor<Booking> bookingCaptor;

    private User testUser;
    private User adminUser;
    private PackageTravel testPackage;
    private Booking testBooking;
    private final LocalDate futureStart = LocalDate.now().plusDays(30);

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .email("viajero@test.com")
                .firstName("Juan")
                .lastName("Perez")
                .role(RoleLogin.USER)
                .enabled(true)
                .phoneNumber("1234567890")
                .build();

        adminUser = User.builder()
                .id(2L)
                .email("admin@tierranativa.com")
                .firstName("Admin")
                .lastName("Nativa")
                .role(RoleLogin.ADMIN)
                .enabled(true)
                .build();

        testPackage = new PackageTravel();
        testPackage.setId(10L);
        testPackage.setName("Glaciar Perito Moreno");
        testPackage.setBasePrice(100000.0);
        testPackage.setCapacity(10);
        testPackage.setNumberOfDays(4);
        testPackage.setDestination("El Calafate");
        testPackage.setShortDescription("Visita al glaciar más famoso de la Patagonia.");
        testPackage.setImages(new ArrayList<>());

        testBooking = Booking.builder()
                .id(100L)
                .startDate(futureStart)
                .endDate(futureStart.plusDays(3))
                .creationDate(LocalDateTime.now())
                .travelerCount(2)
                .totalPrice(200000.0)
                .status("CONFIRMED")
                .packageTravel(testPackage)
                .user(testUser)
                .build();
    }

    @Test
    void createBooking_Success_CalculatesTotalPriceAndSendsEmail() {
        BookingRequestDTO request = buildRequest(futureStart, futureStart.plusDays(3), 2, "1234567890");

        when(packageRepository.findById(10L)).thenReturn(Optional.of(testPackage));
        when(bookingRepository.sumTravelersInOverlap(anyLong(), any(), any())).thenReturn(0);
        when(bookingRepository.save(any(Booking.class))).thenReturn(testBooking);

        bookingService.createBooking(request, testUser);

        verify(bookingRepository).save(bookingCaptor.capture());
        Booking saved = bookingCaptor.getValue();

        assertThat(saved.getTotalPrice()).isEqualTo(100000.0 * 2);
        assertThat(saved.getStatus()).isEqualTo("CONFIRMED");
        assertThat(saved.getTravelerCount()).isEqualTo(2);
        verify(emailService, times(1)).sendBookingConfirmation(any(BookingResponseDTO.class));
    }

    @Test
    void createBooking_StatusDefaultsToConfirmed_WhenNotProvided() {
        BookingRequestDTO request = buildRequest(futureStart, futureStart.plusDays(3), 1, "123");
        request.setStatus(null);

        when(packageRepository.findById(10L)).thenReturn(Optional.of(testPackage));
        when(bookingRepository.sumTravelersInOverlap(anyLong(), any(), any())).thenReturn(0);
        when(bookingRepository.save(any(Booking.class))).thenReturn(testBooking);

        bookingService.createBooking(request, testUser);

        verify(bookingRepository).save(bookingCaptor.capture());
        assertThat(bookingCaptor.getValue().getStatus()).isEqualTo("CONFIRMED");
    }

    @Test
    void createBooking_UpdatesUserPhone_WhenPhoneDiffers() {
        BookingRequestDTO request = buildRequest(futureStart, futureStart.plusDays(3), 1, "NUEVO_NUMERO");

        when(packageRepository.findById(10L)).thenReturn(Optional.of(testPackage));
        when(bookingRepository.sumTravelersInOverlap(anyLong(), any(), any())).thenReturn(0);
        when(bookingRepository.save(any(Booking.class))).thenReturn(testBooking);

        bookingService.createBooking(request, testUser);

        assertThat(testUser.getPhoneNumber()).isEqualTo("NUEVO_NUMERO");
        verify(userRepository).save(testUser);
    }

    @Test
    void createBooking_DoesNotUpdatePhone_WhenPhoneSame() {
        BookingRequestDTO request = buildRequest(futureStart, futureStart.plusDays(3), 1, "1234567890");

        when(packageRepository.findById(10L)).thenReturn(Optional.of(testPackage));
        when(bookingRepository.sumTravelersInOverlap(anyLong(), any(), any())).thenReturn(0);
        when(bookingRepository.save(any(Booking.class))).thenReturn(testBooking);

        bookingService.createBooking(request, testUser);

        verify(userRepository, never()).save(any());
    }

    @Test
    void createBooking_ThrowsResourceNotFoundException_WhenPackageNotFound() {
        BookingRequestDTO request = buildRequest(futureStart, futureStart.plusDays(3), 1, "123");
        request.setPackageId(99L);

        when(packageRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> bookingService.createBooking(request, testUser));
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void createBooking_ThrowsIllegalArgument_WhenDurationMismatch() {
        LocalDate wrongEnd = futureStart.plusDays(10);
        BookingRequestDTO request = buildRequest(futureStart, wrongEnd, 1, "123");

        when(packageRepository.findById(10L)).thenReturn(Optional.of(testPackage));

        assertThatThrownBy(() -> bookingService.createBooking(request, testUser))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("duración");
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void createBooking_ThrowsIllegalState_WhenCapacityExceeded() {
        BookingRequestDTO request = buildRequest(futureStart, futureStart.plusDays(3), 5, "123");

        when(packageRepository.findById(10L)).thenReturn(Optional.of(testPackage));
        when(bookingRepository.sumTravelersInOverlap(anyLong(), any(), any())).thenReturn(8);

        assertThatThrownBy(() -> bookingService.createBooking(request, testUser))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("cupos");
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void createBooking_Success_WhenExactlyAtCapacity() {
        BookingRequestDTO request = buildRequest(futureStart, futureStart.plusDays(3), 2, "123");

        when(packageRepository.findById(10L)).thenReturn(Optional.of(testPackage));
        when(bookingRepository.sumTravelersInOverlap(anyLong(), any(), any())).thenReturn(8);
        when(bookingRepository.save(any(Booking.class))).thenReturn(testBooking);

        bookingService.createBooking(request, testUser);

        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    void cancelBooking_Success_When16DaysAhead() {
        testBooking.setStartDate(LocalDate.now().plusDays(16));
        when(bookingRepository.findById(100L)).thenReturn(Optional.of(testBooking));
        when(bookingRepository.save(any(Booking.class))).thenReturn(testBooking);

        bookingService.cancelBooking(100L, testUser);

        verify(bookingRepository).save(bookingCaptor.capture());
        assertThat(bookingCaptor.getValue().getStatus()).isEqualTo("CANCELLED");
        verify(emailService, times(1)).sendCancellationEmail(any(BookingResponseDTO.class));
    }

    @Test
    void cancelBooking_Success_WhenExactly15DaysAhead() {
        testBooking.setStartDate(LocalDate.now().plusDays(15));
        when(bookingRepository.findById(100L)).thenReturn(Optional.of(testBooking));
        when(bookingRepository.save(any(Booking.class))).thenReturn(testBooking);

        bookingService.cancelBooking(100L, testUser);

        verify(bookingRepository).save(any(Booking.class));
    }

    @Test
    void cancelBooking_ThrowsIllegalState_When14DaysAhead() {
        testBooking.setStartDate(LocalDate.now().plusDays(14));
        when(bookingRepository.findById(100L)).thenReturn(Optional.of(testBooking));

        assertThatThrownBy(() -> bookingService.cancelBooking(100L, testUser))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("15 días");
        verify(bookingRepository, never()).save(any());
        verify(emailService, never()).sendCancellationEmail(any());
    }

    @Test
    void cancelBooking_ThrowsIllegalState_WhenAlreadyCancelled() {
        testBooking.setStatus("CANCELLED");
        when(bookingRepository.findById(100L)).thenReturn(Optional.of(testBooking));

        assertThatThrownBy(() -> bookingService.cancelBooking(100L, testUser))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("cancelada");
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void cancelBooking_ThrowsIllegalState_WhenNotOwnerNorAdmin() {
        User otherUser = User.builder()
                .id(99L)
                .email("otro@test.com")
                .role(RoleLogin.USER)
                .enabled(true)
                .build();
        when(bookingRepository.findById(100L)).thenReturn(Optional.of(testBooking));

        assertThatThrownBy(() -> bookingService.cancelBooking(100L, otherUser))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("permisos");
    }

    @Test
    void cancelBooking_Success_AsAdmin_EvenIfNotOwner() {
        testBooking.setStartDate(LocalDate.now().plusDays(20));
        when(bookingRepository.findById(100L)).thenReturn(Optional.of(testBooking));
        when(bookingRepository.save(any(Booking.class))).thenReturn(testBooking);

        bookingService.cancelBooking(100L, adminUser);

        verify(bookingRepository).save(any(Booking.class));
        verify(emailService).sendCancellationEmail(any(BookingResponseDTO.class));
    }

    @Test
    void cancelBooking_ThrowsResourceNotFound_WhenBookingMissing() {
        when(bookingRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> bookingService.cancelBooking(999L, testUser));
    }

    @Test
    void getBookingById_Success_AsOwner() {
        when(bookingRepository.findById(100L)).thenReturn(Optional.of(testBooking));

        BookingResponseDTO result = bookingService.getBookingById(100L, testUser);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(100L);
    }

    @Test
    void getBookingById_Success_AsAdmin() {
        when(bookingRepository.findById(100L)).thenReturn(Optional.of(testBooking));

        BookingResponseDTO result = bookingService.getBookingById(100L, adminUser);

        assertThat(result).isNotNull();
    }

    @Test
    void getBookingById_ThrowsIllegalState_WhenUnauthorized() {
        User stranger = User.builder()
                .id(55L)
                .email("stranger@test.com")
                .role(RoleLogin.USER)
                .enabled(true)
                .build();
        when(bookingRepository.findById(100L)).thenReturn(Optional.of(testBooking));

        assertThatThrownBy(() -> bookingService.getBookingById(100L, stranger))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("permisos");
    }

    @Test
    void getBookingById_ThrowsResourceNotFound_WhenMissing() {
        when(bookingRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> bookingService.getBookingById(999L, testUser));
    }

    @Test
    void getBookingsByUserId_ReturnsListAndChecksReviewStatus() {
        when(bookingRepository.findByUserIdOrderByCreationDateDesc(1L))
                .thenReturn(List.of(testBooking));
        when(reviewRepository.existsByUserIdAndPackageTravelId(1L, 10L)).thenReturn(true);

        List<BookingResponseDTO> result = bookingService.getBookingsByUserId(1L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getPackageName()).isEqualTo("Glaciar Perito Moreno");
        assertThat(result.get(0).isReviewed()).isTrue();
    }

    @Test
    void getBookingsByUserId_ReturnsEmpty_WhenNoBookings() {
        when(bookingRepository.findByUserIdOrderByCreationDateDesc(1L)).thenReturn(List.of());

        List<BookingResponseDTO> result = bookingService.getBookingsByUserId(1L);

        assertThat(result).isEmpty();
    }

    @Test
    void getAllBookingsForAdmin_ReturnsAllSortedByStartDate() {
        when(bookingRepository.findAllByOrderByStartDateDesc())
                .thenReturn(List.of(testBooking));

        List<BookingResponseDTO> result = bookingService.getAllBookingsForAdmin();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getPackageName()).isEqualTo("Glaciar Perito Moreno");
    }

    @Test
    void updateContactedStatus_SetsContactedTrue() {
        when(bookingRepository.findById(100L)).thenReturn(Optional.of(testBooking));
        when(bookingRepository.save(any(Booking.class))).thenReturn(testBooking);

        bookingService.updateContactedStatus(100L, true);

        verify(bookingRepository).save(bookingCaptor.capture());
        assertThat(bookingCaptor.getValue().isContacted()).isTrue();
    }

    @Test
    void updateContactedStatus_SetsContactedFalse() {
        testBooking.setContacted(true);
        when(bookingRepository.findById(100L)).thenReturn(Optional.of(testBooking));
        when(bookingRepository.save(any(Booking.class))).thenReturn(testBooking);

        bookingService.updateContactedStatus(100L, false);

        verify(bookingRepository).save(bookingCaptor.capture());
        assertThat(bookingCaptor.getValue().isContacted()).isFalse();
    }

    @Test
    void updateContactedStatus_ThrowsResourceNotFound_WhenMissing() {
        when(bookingRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> bookingService.updateContactedStatus(999L, true));
    }

    @Test
    void getAvailableSpots_ReturnsCorrectRemainingCapacity() {
        LocalDate start = LocalDate.now().plusDays(30);
        LocalDate end = start.plusDays(3);

        when(packageRepository.findById(10L)).thenReturn(Optional.of(testPackage));
        when(bookingRepository.sumTravelersInOverlap(10L, start, end)).thenReturn(3);

        int available = bookingService.getAvailableSpots(10L, start, end);

        assertThat(available).isEqualTo(7); // capacity(10) - booked(3)
    }

    @Test
    void getAvailableSpots_ReturnsZero_WhenFullyBooked() {
        LocalDate start = LocalDate.now().plusDays(30);
        LocalDate end = start.plusDays(3);

        when(packageRepository.findById(10L)).thenReturn(Optional.of(testPackage));
        when(bookingRepository.sumTravelersInOverlap(10L, start, end)).thenReturn(10);

        int available = bookingService.getAvailableSpots(10L, start, end);

        assertThat(available).isZero();
    }

    @Test
    void getAvailableSpots_ReturnsZero_WhenOverbooked() {
        LocalDate start = LocalDate.now().plusDays(30);
        LocalDate end = start.plusDays(3);

        when(packageRepository.findById(10L)).thenReturn(Optional.of(testPackage));
        when(bookingRepository.sumTravelersInOverlap(10L, start, end)).thenReturn(15);

        int available = bookingService.getAvailableSpots(10L, start, end);

        assertThat(available).isZero();
    }

    @Test
    void getAvailableSpots_ThrowsResourceNotFound_WhenPackageMissing() {
        when(packageRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> bookingService.getAvailableSpots(99L,
                        LocalDate.now().plusDays(1), LocalDate.now().plusDays(4)));
    }

    private BookingRequestDTO buildRequest(LocalDate start, LocalDate end,
                                           int travelers, String phone) {
        BookingRequestDTO req = new BookingRequestDTO();
        req.setPackageId(10L);
        req.setStartDate(start);
        req.setEndDate(end);
        req.setTravelerCount(travelers);
        req.setPhoneNumber(phone);
        req.setStatus("CONFIRMED");
        return req;
    }
}