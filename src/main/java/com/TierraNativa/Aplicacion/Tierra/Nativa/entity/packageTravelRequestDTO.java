package com.tierranativa.aplicacion.tierra.nativa.entity;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class packageTravelRequestDTO {

    @Size(min = 3, max = 100, message = "El nombre debe tener minimo 3 caracteres.")
    @NotBlank(message = "El nombre del paquete es obligatorio.")
    private String name;

    @NotNull
    @Valid
    private itineraryDetailDTO itineraryDetail;

    @NotNull(message = "El precio base es obligatorio.")
    @DecimalMin(value = "0.01", message = "El precio debe ser positivo.")
    private Double basePrice;

    @NotNull
    @Valid
    @Size(min = 1, message = "Debe haber al menos una imagen.")
    private List<imageDTO> imageDetails;

    @Size(min = 10, max = 150, message = "El nombre debe tener minimo 10 caracteres.")
    @NotBlank(message = "La descripción del paquete es obligatoria.")
    private String shortDescription;

    @Size(min = 3, max = 100, message = "El nombre debe tener minimo 3 caracteres.")
    @NotBlank(message = "El destino del paquete es obligatorio.")
    private String destination;

    @NotBlank(message = "La categoría del paquete es obligatoria.")
    private String category;
}

