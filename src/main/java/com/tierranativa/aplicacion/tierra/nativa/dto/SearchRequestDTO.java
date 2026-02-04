package com.tierranativa.aplicacion.tierra.nativa.dto;

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
}