package com.tierranativa.aplicacion.tierra.nativa.repository;

import com.tierranativa.aplicacion.tierra.nativa.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking , Long> {

    @Query ("""
            SELECT COUNT(b) > 0 FROM Booking b
            WHERE b.packageTravel.id = :packageId
            AND b.status = 'CONFIRMED'
            AND (:startDate <= b.endDate AND :endDate >= b.startDate)
            """)

    boolean existOverlap(
            @Param("packageId") Long packageId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
            );

    @Query("""
            SELECT COALESCE(SUM(b.travelerCount), 0) FROM Booking b
            WHERE b.packageTravel.id = :packageId
            AND b.status = 'CONFIRMED'
            AND (:startDate <= b.endDate AND :endDate >= b.startDate)
            """)
    Integer sumTravelersInOverlap(
            @Param("packageId") Long packageId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    boolean existsByUserIdAndPackageTravelIdAndStatus(Long userId, Long packageTravelId, String status);

    List<Booking> findByPackageTravelId(Long packageId);

    List<Booking> findByUserId(Long userId);

    List<Booking> findByUserIdOrderByCreationDateDesc(Long userId);

    List<Booking> findByPackageTravelIdAndStatus(Long packageId, String status);
}
