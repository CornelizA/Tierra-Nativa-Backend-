package com.tierranativa.aplicacion.tierra.nativa.dto;

import com.tierranativa.aplicacion.tierra.nativa.entity.PackageItineraryDetail;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItineraryDetailDTO {

    @NotBlank(message = "La duración del paquete es obligatoria.")
    @Size(min = 5, max = 100, message = "Duración: debe contener entre 5 y 100 caracteres.")
    private String duration;

    @NotBlank(message = "El tipo de hospedaje es obligatorio.")
    @Size(min = 5, max = 255, message = "Hospedaje: el texto debe tener entre 5 y 255 caracteres.")
    private String lodgingType;

    @NotBlank(message = "El tipo de traslado es obligatorio.")
    @Size(min = 5, max = 255, message = "Traslado: el texto debe tener entre 5 y 255 caracteres.")
    private String transferType;

    @NotBlank(message = "Las actividades diarias son obligatorias.")
    @Size(min = 10, max = 5000, message = "Descripción de actividades: se requiere un texto de entre 10 y 5000 caracteres.")
    private String dailyActivitiesDescription;

    @NotBlank(message = "Las notas de alimentación son obligatorias.")
    @Size(min = 10, max = 2000, message = "Alimentación: las notas deben tener entre 10 y 2000 caracteres.")
    private String foodAndHydrationNotes;

    @NotBlank(message = "Las recomendaciones son obligatorias.")
    @Size(min = 10, max = 3000, message = "Recomendaciones: el texto debe tener entre 10 y 3000 caracteres.")
    private String generalRecommendations;

    public static ItineraryDetailDTO fromEntity(PackageItineraryDetail detail) {
        if (detail == null) {
            return null;
        }
        return ItineraryDetailDTO.builder()
                .duration(detail.getDuration())
                .lodgingType(detail.getLodgingType())
                .transferType(detail.getTransferType())
                .dailyActivitiesDescription(detail.getDailyActivitiesDescription())
                .foodAndHydrationNotes(detail.getFoodAndHydrationNotes())
                .generalRecommendations(detail.getGeneralRecommendations())
                .build();
    }
}
