package com.TierraNativa.Aplicacion.Tierra.Nativa.service;

import com.TierraNativa.Aplicacion.Tierra.Nativa.entity.PackageTravel;
import com.TierraNativa.Aplicacion.Tierra.Nativa.entity.PackageTravelRequestDTO;
import java.util.List;
import java.util.Optional;

public interface PackageTravelService {

    PackageTravel registerNewPackage(PackageTravelRequestDTO requestDto) throws Exception;

    Optional<PackageTravel> findById(Long id);

    void update(PackageTravel packageTravel);

    void delete(Long id);

    List<PackageTravel> findAll();

    List<PackageTravel> findByCategory(String category);

}
