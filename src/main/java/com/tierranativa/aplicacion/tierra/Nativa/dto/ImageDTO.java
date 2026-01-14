package com.tierranativa.aplicacion.tierra.nativa.dto;

import com.tierranativa.aplicacion.tierra.nativa.entity.PackageImage;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ImageDTO {

    private Long id;

    @NotBlank(message = "La URL de la imagen es obligatoria.")
    private String url;

    @NotNull(message = "El indicador principal es obligatorio.")
    private Boolean principal;

    public static ImageDTO fromEntity(PackageImage image) {
        if (image == null) {
            return null;
        }
        return ImageDTO.builder()
                .id(image.getId())
                .url(image.getUrl())
                .principal(image.getPrincipal())
                .build();
    }
}


