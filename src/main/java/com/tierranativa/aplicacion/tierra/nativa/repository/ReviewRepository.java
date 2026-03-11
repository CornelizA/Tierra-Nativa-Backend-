package com.tierranativa.aplicacion.tierra.nativa.repository;

import com.tierranativa.aplicacion.tierra.nativa.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByPackageTravelIdOrderByCreatedAtDesc(Long packageId);

    @Query("SELECT AVG(r.score) FROM Review r WHERE r.packageTravel.id = :packageId")
    Double calculateAverageScoreByPackageId(@Param("packageId") Long packageId);

    boolean existsByUserIdAndPackageTravelId(Long userId, Long packageId);
}
