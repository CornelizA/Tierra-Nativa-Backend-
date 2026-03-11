package com.tierranativa.aplicacion.tierra.nativa.controller;

import com.tierranativa.aplicacion.tierra.nativa.dto.PackageTravelRequestDTO;
import com.tierranativa.aplicacion.tierra.nativa.entity.User;
import com.tierranativa.aplicacion.tierra.nativa.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/favorites")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    @PostMapping("/toggle/{packageId}")
    public ResponseEntity<Map<String, Boolean>> toggleFavorite(@PathVariable Long packageId, @AuthenticationPrincipal User user) {
        boolean isFavorite = favoriteService.toggleFavorite(user.getId(), packageId);
        return ResponseEntity.ok(Map.of("isFavorite", isFavorite));
    }

    @GetMapping
    public ResponseEntity<List<PackageTravelRequestDTO>> getMyFavorites(
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(favoriteService.getFavoritesByUser(user.getId()));
    }
}