package com.tierranativa.aplicacion.tierra.nativa.repository;

import com.tierranativa.aplicacion.tierra.nativa.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    boolean existsByUserIdAndPackageTravelId(Long userId, Long packageId);

    Optional<Favorite> findByUserIdAndPackageTravelId(Long userId, Long packageId);

    List<Favorite> findByUserId(Long userId);

    @Transactional
    void deleteByUserIdAndPackageTravelId(Long userId, Long packageId);
}
