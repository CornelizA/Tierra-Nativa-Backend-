package com.tierranativa.aplicacion.tierra.nativa.service.implement;

import com.tierranativa.aplicacion.tierra.nativa.dto.PackageTravelRequestDTO;
import com.tierranativa.aplicacion.tierra.nativa.entity.Favorite;
import com.tierranativa.aplicacion.tierra.nativa.repository.FavoriteRepository;
import com.tierranativa.aplicacion.tierra.nativa.repository.PackageTravelRepository;
import com.tierranativa.aplicacion.tierra.nativa.repository.UserRepository;
import com.tierranativa.aplicacion.tierra.nativa.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IFavoriteService implements FavoriteService {

        private final FavoriteRepository favoriteRepository;
        private final UserRepository userRepository;
        private final PackageTravelRepository packageRepository;

    @Override
    @Transactional
    public boolean toggleFavorite(Long userId, Long packageId) {
        if (favoriteRepository.existsByUserIdAndPackageTravelId(userId, packageId)) {
            favoriteRepository.deleteByUserIdAndPackageTravelId(userId, packageId);
            return false;
        } else {
            var user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + userId));
            var pkg = packageRepository.findById(packageId)
                    .orElseThrow(() -> new RuntimeException("Paquete no encontrado con ID: " + packageId));

            favoriteRepository.save(Favorite.builder()
                    .user(user)
                    .packageTravel(pkg)
                    .build());
            return true;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<PackageTravelRequestDTO> getFavoritesByUser(Long userId) {
        return favoriteRepository.findByUserId(userId).stream()
                .map(f -> PackageTravelRequestDTO.fromEntity(f.getPackageTravel(), null, true))
                .toList();
    }
}