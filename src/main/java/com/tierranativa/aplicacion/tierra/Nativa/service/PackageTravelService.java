package com.tierranativa.aplicacion.tierra.nativa.service;

import com.tierranativa.aplicacion.tierra.nativa.entity.Category;
import com.tierranativa.aplicacion.tierra.nativa.entity.PackageTravel;
import com.tierranativa.aplicacion.tierra.nativa.dto.PackageTravelRequestDTO;
import com.tierranativa.aplicacion.tierra.nativa.entity.User;

import java.util.List;
import java.util.Optional;

public interface PackageTravelService {

    PackageTravel registerNewPackage(PackageTravelRequestDTO requestDto, User admin) throws Exception;

    Optional<PackageTravel> findById(Long id);

    PackageTravel update(Long id, PackageTravelRequestDTO updateDto, User admin) throws Exception;

    void delete(Long id);

    List<PackageTravel> findAll();

    List<PackageTravel> findByCategoryTitle(String categoryTitle);

    Optional<Category> findCategoryByTitle(String categoryTitle);
}
