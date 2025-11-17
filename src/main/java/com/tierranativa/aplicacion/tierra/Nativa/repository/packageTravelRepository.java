package com.tierranativa.aplicacion.tierra.nativa.repository;

import com.tierranativa.aplicacion.tierra.nativa.entity.packageCategory;
import com.tierranativa.aplicacion.tierra.nativa.entity.packageTravel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface packageTravelRepository extends JpaRepository<packageTravel, Long> {

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, Long id);

    List<packageTravel> findByCategory(packageCategory category);
}
