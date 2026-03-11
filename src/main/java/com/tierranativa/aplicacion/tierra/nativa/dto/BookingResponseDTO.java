package com.tierranativa.aplicacion.tierra.nativa.dto;

import com.tierranativa.aplicacion.tierra.nativa.entity.Booking;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookingResponseDTO {

    private Long id;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
    private Long packageId;
    private String packageName;
    private Long userId;
    private String userEmail;

    public static BookingResponseDTO fromEntity(Booking booking) {
        if (booking == null) return null;
        return BookingResponseDTO.builder()
                .id(booking.getId())
                .startDate(booking.getStartDate())
                .endDate(booking.getEndDate())
                .status(booking.getStatus())
                .packageId(booking.getPackageTravel().getId())
                .packageName(booking.getPackageTravel().getName())
                .userId(booking.getUser().getId())
                .userEmail(booking.getUser().getEmail())
                .build();
    }
}