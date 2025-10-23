package com.TierraNativa.Aplicacion.Tierra.Nativa.repository;

import com.TierraNativa.Aplicacion.Tierra.Nativa.entity.PackageTravel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PackageTravelRepository extends JpaRepository<PackageTravel, Long> {

    List<PackageTravel> findByDestination(String destination);
}
