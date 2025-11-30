package com.tierranativa.aplicacion.tierra.nativa.repository;

import com.tierranativa.aplicacion.tierra.nativa.entity.PackageCategory;
import com.tierranativa.aplicacion.tierra.nativa.entity.PackageTravel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PackageTravelRepository extends JpaRepository<PackageTravel, Long> {

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, Long id);

    List<PackageTravel> findByCategory(PackageCategory category);
}
