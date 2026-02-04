package com.tierranativa.aplicacion.tierra.nativa.repository;

import com.tierranativa.aplicacion.tierra.nativa.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking , Long> {

    boolean existsByUserIdAndPackageTravelIdAndStatus(Long userId, Long packageTravelId, String status);

    List<Booking> findByPackageTravelId(Long packageId);

    List<Booking> findByUserId(Long userId);
}
