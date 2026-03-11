package com.tierranativa.aplicacion.tierra.nativa.repository;

import com.tierranativa.aplicacion.tierra.nativa.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking , Long> {

    boolean existsByUserIdAndPackageTravelIdAndStatus(Long userId, Long packageTravelId, String status);

    List<Booking> findByPackageTravelId(Long packageId);

    List<Booking> findByUserId(Long userId);
}
