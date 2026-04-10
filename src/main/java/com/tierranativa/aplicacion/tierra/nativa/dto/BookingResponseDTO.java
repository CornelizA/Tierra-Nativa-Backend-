package com.tierranativa.aplicacion.tierra.nativa.dto;

import com.tierranativa.aplicacion.tierra.nativa.entity.Booking;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookingResponseDTO {

    private Long id;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDateTime creationDate;
    private String specialRequests;
    private String status;
    private Long packageId;
    private String packageName;
    private String packageDestination;
    private String packageDescription;
    private String packageImageUrl;
    private Long userId;
    private String userEmail;
    private String userFirstName;
    private String userLastName;
    private Double packageBasePrice;
    private Integer packageCapacity;
    private Integer travelerCount;
    private Double totalPrice;
    private boolean reviewed;
    private String userPhone;
    private boolean isContacted;


    public static BookingResponseDTO fromEntity(Booking booking) {
        return fromEntity(booking, false);
    }

    public static BookingResponseDTO fromEntity(Booking booking, boolean reviewed) {
        if (booking == null) return null;
        String imageUrl = (booking.getPackageTravel().getImages() != null && !booking.getPackageTravel().getImages().isEmpty())
                ? booking.getPackageTravel().getImages().get(0).getUrl()
                : booking.getPackageTravel().getImageUrl();
        return BookingResponseDTO.builder()
                .id(booking.getId())
                .startDate(booking.getStartDate())
                .endDate(booking.getEndDate())
                .creationDate(booking.getCreationDate())
                .specialRequests(booking.getSpecialRequests())
                .status(booking.getStatus())
                .packageId(booking.getPackageTravel().getId())
                .packageName(booking.getPackageTravel().getName())
                .packageDestination(booking.getPackageTravel().getDestination())
                .packageDescription(booking.getPackageTravel().getShortDescription())
                .packageImageUrl(imageUrl)
                .packageBasePrice(booking.getPackageTravel().getBasePrice())
                .packageCapacity(booking.getPackageTravel().getCapacity())
                .travelerCount(booking.getTravelerCount())
                .totalPrice(booking.getTotalPrice())
                .userId(booking.getUser().getId())
                .userEmail(booking.getUser().getEmail())
                .userFirstName(booking.getUser().getFirstName())
                .userLastName(booking.getUser().getLastName())
                .userPhone(booking.getUser().getPhoneNumber())
                .reviewed(reviewed)
                .isContacted(booking.isContacted())
                .build();
    }
}