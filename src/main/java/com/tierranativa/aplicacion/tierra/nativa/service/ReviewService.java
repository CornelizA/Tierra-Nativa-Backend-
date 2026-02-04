package com.tierranativa.aplicacion.tierra.nativa.service;

import com.tierranativa.aplicacion.tierra.nativa.dto.ReviewDTO;
import com.tierranativa.aplicacion.tierra.nativa.dto.ReviewRequestDTO;

import java.util.List;

public interface ReviewService {

    ReviewDTO addReview(ReviewRequestDTO reviewRequestDTO, Long id);

    List<ReviewDTO> getReviewsByPackage(Long id);
}
