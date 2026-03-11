package com.tierranativa.aplicacion.tierra.nativa.service;

import com.tierranativa.aplicacion.tierra.nativa.dto.PackageTravelRequestDTO;

import java.util.List;

public interface FavoriteService {

    boolean toggleFavorite(Long userId, Long packageId);

    List<PackageTravelRequestDTO> getFavoritesByUser(Long userId);
}

