package com.tierranativa.aplicacion.tierra.nativa.repository;


import com.tierranativa.aplicacion.tierra.nativa.entity.PackageCharacteristics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CharacteristicRepository extends JpaRepository<PackageCharacteristics, Long> {

    Optional<PackageCharacteristics> findByTitle(String title);
}
