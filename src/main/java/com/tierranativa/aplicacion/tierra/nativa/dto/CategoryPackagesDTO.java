package com.tierranativa.aplicacion.tierra.nativa.dto;
import com.tierranativa.aplicacion.tierra.nativa.entity.Category;
import com.tierranativa.aplicacion.tierra.nativa.entity.PackageTravel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryPackagesDTO {

    private Category categoryDetails;

    private List<PackageTravelRequestDTO> packages;

    public static CategoryPackagesDTO from(Category category, List<PackageTravel> packageTravels) {
        if (category == null) {
            return CategoryPackagesDTO.builder().categoryDetails(null).packages(List.of()).build();
        }

        final Long categoryIdToExclude = category.getId();

        List<PackageTravelRequestDTO> dtoList = packageTravels.stream()
                .map(pkg -> PackageTravelRequestDTO.fromEntity(pkg, categoryIdToExclude))
                .collect(Collectors.toList());

        return CategoryPackagesDTO.builder()
                .categoryDetails(category)
                .packages(dtoList)
                .build();
    }
}