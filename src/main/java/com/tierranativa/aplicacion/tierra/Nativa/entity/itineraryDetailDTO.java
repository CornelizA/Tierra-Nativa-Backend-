package com.tierranativa.aplicacion.tierra.nativa.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data

public class itineraryDetailDTO {

    @Size(min = 10, max = 100, message = "El texto debe tener minimo 10 caracteres.")
    @NotBlank(message = "La duración del paquete es obligatoria.")
    private String duration;

    @Size(min = 10, max = 150, message = "El texto debe tener minimo 10 caracteres.")
    @NotBlank(message = "El tipo de hospedaje del paquete es obligatorio.")
    private String lodgingType;

    @Size(min = 10, max = 150, message = "El texto debe tener minimo 10 caracteres.")
    @NotBlank(message = "El tipo de traslado del paquete es obligatorio.")
    private String transferType;

    @Size(min = 10, max = 300, message = "El texto debe tener minimo 10 caracteres.")
    @NotBlank(message = "Las actividades diarias del paquete son  obligatorias.")
    private String dailyActivitiesDescription;

    @Size(min = 10, max = 300, message = "El texto debe tener minimo 10 caracteres.")
    @NotBlank(message = "La alimentación e hidratación del paquete es obligatoria.")
    private String foodAndHydrationNotes;

    @Size(min = 10, max = 400, message = "El texto debe tener minimo 10 caracteres.")
    @NotBlank(message = "Las recomendaciones del paquete son obligatorias.")
    private String generalRecommendations;
}
