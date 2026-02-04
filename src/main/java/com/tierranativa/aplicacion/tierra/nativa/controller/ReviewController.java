package com.tierranativa.aplicacion.tierra.nativa.controller;

import com.tierranativa.aplicacion.tierra.nativa.dto.ReviewDTO;
import com.tierranativa.aplicacion.tierra.nativa.dto.ReviewRequestDTO;
import com.tierranativa.aplicacion.tierra.nativa.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
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

    @GetMapping("/package/{packageId}")
    public ResponseEntity<List<ReviewDTO>> getReviews(@PathVariable Long packageId) {
        List<ReviewDTO> reviews = reviewService.getReviewsByPackage(packageId);
        return ResponseEntity.ok(reviews);
    }
}