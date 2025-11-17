package com.tierranativa.aplicacion.tierra.nativa.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class imageDTO {

    @NotBlank(message = "La URL de la imagen es obligatoria.")
    private String url;

    @NotNull(message = "El indicador principal es obligatorio.")
    private Boolean principal;
}

