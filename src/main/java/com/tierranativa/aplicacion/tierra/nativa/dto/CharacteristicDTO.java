package com.tierranativa.aplicacion.tierra.nativa.dto;

import com.tierranativa.aplicacion.tierra.nativa.entity.PackageCharacteristics;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class CharacteristicDTO {

    private Long id;

    @Size(min = 3, max = 100, message = "El nombre debe tener minimo 3 caracteres.")
    @NotBlank(message = "El nombre de la característica es obligatorio.")
    private String title;

    @NotNull(message = "El icono es obligatorio.")
    private String icon;

    public static CharacteristicDTO fromEntity(PackageCharacteristics characteristic) {
        if (characteristic == null) return null;

        return CharacteristicDTO.builder()
                .id(characteristic.getId())
                .title(characteristic.getTitle())
                .icon(characteristic.getIcon())
                .build();
    }

    public static List<CharacteristicDTO> fromEntityList(List<PackageCharacteristics> characteristics) {
        return characteristics.stream()
                .map(CharacteristicDTO::fromEntity)
                .collect(Collectors.toList());
    }
}


