package com.tierranativa.aplicacion.tierra.nativa.service;

import com.tierranativa.aplicacion.tierra.nativa.entity.Category;
import com.tierranativa.aplicacion.tierra.nativa.entity.PackageTravel;
import com.tierranativa.aplicacion.tierra.nativa.dto.PackageTravelRequestDTO;
import java.util.List;
import java.util.Optional;

public interface PackageTravelService {

    PackageTravel registerNewPackage(PackageTravelRequestDTO requestDto) throws Exception;

    Optional<PackageTravel> findById(Long id);

    PackageTravel update(PackageTravel packageTravel);

    void delete(Long id);

    List<PackageTravel> findAll();

    List<PackageTravel> findByCategoryTitle(String categoryTitle);

    Optional<Category> findCategoryByTitle(String categoryTitle);
}
