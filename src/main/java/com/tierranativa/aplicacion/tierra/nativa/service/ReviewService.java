package com.tierranativa.aplicacion.tierra.nativa.service;

import com.tierranativa.aplicacion.tierra.nativa.dto.ReviewDTO;
import com.tierranativa.aplicacion.tierra.nativa.dto.ReviewRequestDTO;

import java.util.List;

public interface ReviewService {

    ReviewDTO addReview(ReviewRequestDTO reviewRequestDTO, Long id);

    ReviewDTO updateReview(ReviewRequestDTO reviewRequestDTO, Long reviewId, Long userId);

    void deleteReview(Long reviewId, Long userId);

    List<ReviewDTO> getReviewsByPackage(Long id);
}
