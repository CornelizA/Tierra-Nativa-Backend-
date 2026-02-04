package com.tierranativa.aplicacion.tierra.nativa.service.implement;

import com.tierranativa.aplicacion.tierra.nativa.dto.ReviewDTO;
import com.tierranativa.aplicacion.tierra.nativa.dto.ReviewRequestDTO;
import com.tierranativa.aplicacion.tierra.nativa.entity.Review;
import com.tierranativa.aplicacion.tierra.nativa.entity.PackageTravel;
import com.tierranativa.aplicacion.tierra.nativa.entity.User;
import com.tierranativa.aplicacion.tierra.nativa.exception.ResourceNotFoundException;
import com.tierranativa.aplicacion.tierra.nativa.repository.BookingRepository;
import com.tierranativa.aplicacion.tierra.nativa.repository.ReviewRepository;
import com.tierranativa.aplicacion.tierra.nativa.repository.PackageTravelRepository;
import com.tierranativa.aplicacion.tierra.nativa.repository.UserRepository;
import com.tierranativa.aplicacion.tierra.nativa.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IReviewService implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final PackageTravelRepository packageRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;

    @Override
    @Transactional
    public ReviewDTO addReview(ReviewRequestDTO request, Long userId) {

        boolean hasFinishedTrip = bookingRepository.existsByUserIdAndPackageTravelIdAndStatus(
                userId, request.getPackageId(), "FINISHED");

        if (!hasFinishedTrip) {
            throw new IllegalStateException("Para puntuar un producto debes haber completado el viaje.");
        }

        if (reviewRepository.existsByUserIdAndPackageTravelId(userId, request.getPackageId())) {
            throw new IllegalStateException("Ya has calificado este paquete anteriormente.");
        }

        PackageTravel pkg = packageRepository.findById(request.getPackageId())
                .orElseThrow(() -> new ResourceNotFoundException("Paquete no encontrado"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        Review review = Review.builder()
                .score(request.getScore())
                .comment(request.getComment())
                .createdAt(LocalDateTime.now())
                .user(user)
                .packageTravel(pkg)
                .build();

        Review saved = reviewRepository.save(review);
        updatePackageAverageScore(pkg);

        return mapToDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewDTO> getReviewsByPackage(Long packageId) {
        return reviewRepository.findByPackageTravelIdOrderByCreatedAtDesc(packageId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private ReviewDTO mapToDTO(Review review) {
        return ReviewDTO.builder()
                .id(review.getId())
                .score(review.getScore())
                .comment(review.getComment())
                .userName(review.getUser().getFirstName() + " " + review.getUser().getLastName())
                .date(review.getCreatedAt())
                .build();
    }

    private void updatePackageAverageScore(PackageTravel pkg) {
        Double average = reviewRepository.calculateAverageScoreByPackageId(pkg.getId());
        pkg.setAverageRating(average != null ? average : 0.0);
        packageRepository.save(pkg);
    }
}