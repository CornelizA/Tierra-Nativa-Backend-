package com.tierranativa.aplicacion.tierra.nativa.dto;

import jakarta.validation.constraints.AssertTrue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchRequestDTO {

    private String keyword;
    private LocalDate checkIn;
    private LocalDate checkOut;

    @AssertTrue(message = "Si se especifica una fecha, ambas fechas (checkIn y checkOut) son obligatorias")
    public boolean isDatesConsistent() {
        boolean hasCheckIn = checkIn != null;
        boolean hasCheckOut = checkOut != null;
        return hasCheckIn == hasCheckOut;
    }
}