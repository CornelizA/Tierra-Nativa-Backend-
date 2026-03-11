package com.tierranativa.aplicacion.tierra.nativa.controller;

import com.tierranativa.aplicacion.tierra.nativa.dto.ReviewDTO;
import com.tierranativa.aplicacion.tierra.nativa.dto.ReviewRequestDTO;
import com.tierranativa.aplicacion.tierra.nativa.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.tierranativa.aplicacion.tierra.nativa.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewDTO> createReview(@Valid @RequestBody ReviewRequestDTO request, @AuthenticationPrincipal User user) {
        ReviewDTO createdReview = reviewService.addReview(request, user.getId());
        return new ResponseEntity<>(createdReview, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReviewDTO> updateReview(@PathVariable Long id, @Valid @RequestBody ReviewRequestDTO request, @AuthenticationPrincipal User user) {
        ReviewDTO updatedReview = reviewService.updateReview(request, id, user.getId());
        return ResponseEntity.ok(updatedReview);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id, @AuthenticationPrincipal User user) {
        reviewService.deleteReview(id, user.getId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/package/{packageId}")
    public ResponseEntity<List<ReviewDTO>> getReviews(@PathVariable Long packageId) {
        List<ReviewDTO> reviews = reviewService.getReviewsByPackage(packageId);
        return ResponseEntity.ok(reviews);
    }
}