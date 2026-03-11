package com.tierranativa.aplicacion.tierra.nativa.repository;

import com.tierranativa.aplicacion.tierra.nativa.entity.PackageTravel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SearchRepository extends JpaRepository<PackageTravel, Long> {

    @Query("""
        SELECT p FROM PackageTravel p 
        WHERE (LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) 
           OR LOWER(p.destination) LIKE LOWER(CONCAT('%', :keyword, '%')))
        AND p.id NOT IN (
            SELECT b.packageTravel.id FROM Booking b 
            WHERE b.status = 'CONFIRMED'
            AND (:start <= b.endDate AND :end >= b.startDate)
        )
    """)
    List<PackageTravel> findAvailablePackages(
            @Param("keyword") String keyword,
            @Param("start") LocalDate start,
            @Param("end") LocalDate end
    );

    @Query("""
        SELECT p FROM PackageTravel p 
        WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) 
           OR LOWER(p.destination) LIKE LOWER(CONCAT('%', :keyword, '%'))
    """)
    List<PackageTravel> findByKeywordOnly(@Param("keyword") String keyword);
}