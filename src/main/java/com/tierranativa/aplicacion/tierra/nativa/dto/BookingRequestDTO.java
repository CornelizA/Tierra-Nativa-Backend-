package com.tierranativa.aplicacion.tierra.nativa.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class BookingRequestDTO {

    @NotNull(message = "El ID del paquete es obligatorio")
    private Long packageId;

    @NotNull(message = "La fecha de inicio es obligatoria")
    private LocalDate startDate;

    @NotNull(message = "La fecha de fin es obligatoria")
    private LocalDate endDate;

    @NotNull(message = "La cantidad de personas es obligatoria")
    @Min(value = 1, message = "Debe haber al menos 1 viajero")
    private Integer travelerCount;

    @NotBlank(message = "El número de teléfono es obligatorio")
    private String phoneNumber;

    private LocalDateTime creationDate;

    private String specialRequests;

    private String status;
}
