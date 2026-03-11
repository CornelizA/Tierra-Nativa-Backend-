package com.tierranativa.aplicacion.tierra.nativa.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AvailabilityBlockDTO {

    private LocalDate startDate;
    private LocalDate endDate;
}