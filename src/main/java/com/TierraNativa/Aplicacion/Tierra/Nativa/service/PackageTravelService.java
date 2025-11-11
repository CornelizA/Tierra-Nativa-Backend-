package com.TierraNativa.Aplicacion.Tierra.Nativa.service;

import com.TierraNativa.Aplicacion.Tierra.Nativa.entity.PackageCategory;
import com.TierraNativa.Aplicacion.Tierra.Nativa.entity.PackageTravel;
import com.TierraNativa.Aplicacion.Tierra.Nativa.entity.PackageTravelRequestDTO;
import java.util.List;
import java.util.Optional;

public interface PackageTravelService {

    List<PackageTravel> findByCategory(PackageCategory category);

    PackageTravel registerNewPackage(PackageTravelRequestDTO requestDto) throws Exception;

    Optional<PackageTravel> findById(Long id);

    PackageTravel update(PackageTravel packageTravel);

    void delete(Long id);

    List<PackageTravel> findAll();
}
