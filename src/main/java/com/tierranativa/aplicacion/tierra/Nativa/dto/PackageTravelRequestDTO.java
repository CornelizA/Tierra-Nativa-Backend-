package com.tierranativa.aplicacion.tierra.nativa.dto;

import com.tierranativa.aplicacion.tierra.nativa.entity.Category;
import com.tierranativa.aplicacion.tierra.nativa.entity.PackageCharacteristics;
import com.tierranativa.aplicacion.tierra.nativa.entity.PackageTravel;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PackageTravelRequestDTO {

    private Long id;

    @Size(min = 3, max = 100, message = "El nombre debe tener minimo 3 caracteres.")
    @NotBlank(message = "El nombre del paquete es obligatorio.")
    private String name;

    @NotNull
    @Valid
    private ItineraryDetailDTO itineraryDetail;

    @NotNull(message = "El precio base es obligatorio.")
    @DecimalMin(value = "0.01", message = "El precio debe ser positivo.")
    private Double basePrice;

    @NotNull(message = "La lista de imágenes no puede ser nula.")
    @Valid
    @Size(min = 1, message = "Debe haber al menos una imagen.")
    private List<ImageDTO> imageDetails;

    @Size(min = 10, max = 500, message = "La descripción debe tener entre 10 y 500 caracteres.")
    @NotBlank(message = "La descripción del paquete es obligatoria.")
    private String shortDescription;

    @Size(min = 3, max = 100, message = "El nombre debe tener minimo 3 caracteres.")
    @NotBlank(message = "El destino del paquete es obligatorio.")
    private String destination;

    @Size(min = 1, message = "Debe seleccionar al menos una categoría.")
    @NotNull(message = "La selección de categorías es obligatoria.")
    private Set<Long> categoryId;

    @Size(min = 1, message = "Debe seleccionar al menos una característica.")
    @NotNull(message = "La selección de características es obligatoria.")
    private Set<Long> characteristicIds;

    public static PackageTravelRequestDTO fromEntity(PackageTravel packageTravel, Long excludeCategoryId) {
        if (packageTravel == null) return null;

        Set<Long> filteredCategoryIds = packageTravel.getCategories() != null
                ? packageTravel.getCategories().stream()
                .map(Category::getId)
                .filter(id -> excludeCategoryId == null || !id.equals(excludeCategoryId))
                .collect(Collectors.toSet())
                : Collections.emptySet();

        Set<Long> charIds = packageTravel.getCharacteristics() != null
                ? packageTravel.getCharacteristics().stream()
                .map(PackageCharacteristics::getId)
                .collect(Collectors.toSet())
                : Collections.emptySet();

        List<ImageDTO> imageDtoList = packageTravel.getImages() != null
                ? packageTravel.getImages().stream()
                .map(ImageDTO::fromEntity)
                .collect(Collectors.toList())
                : Collections.emptyList();

        ItineraryDetailDTO itineraryDTO = packageTravel.getItineraryDetail() != null
                ? ItineraryDetailDTO.fromEntity(packageTravel.getItineraryDetail())
                : null;

        return PackageTravelRequestDTO.builder()
                .id(packageTravel.getId())
                .name(packageTravel.getName())
                .basePrice(packageTravel.getBasePrice())
                .shortDescription(packageTravel.getShortDescription())
                .destination(packageTravel.getDestination())
                .imageDetails(imageDtoList)
                .itineraryDetail(itineraryDTO)
                .categoryId(filteredCategoryIds)
                .characteristicIds(charIds)
                .build();
    }

    public static PackageTravelRequestDTO fromEntity(PackageTravel packageTravel) {
        return fromEntity(packageTravel, null);
    }
}