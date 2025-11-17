package com.tierranativa.aplicacion.tierra.nativa.service;

import com.tierranativa.aplicacion.tierra.nativa.entity.packageCategory;
import com.tierranativa.aplicacion.tierra.nativa.entity.packageTravel;
import com.tierranativa.aplicacion.tierra.nativa.entity.packageTravelRequestDTO;
import java.util.List;
import java.util.Optional;

public interface packageTravelService {

    List<packageTravel> findByCategory(packageCategory category);

    packageTravel registerNewPackage(packageTravelRequestDTO requestDto) throws Exception;

    Optional<packageTravel> findById(Long id);

    packageTravel update(packageTravel packageTravel);

    void delete(Long id);

    List<packageTravel> findAll();
}
