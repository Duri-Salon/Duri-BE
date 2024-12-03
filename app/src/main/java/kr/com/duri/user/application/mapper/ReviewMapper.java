package kr.com.duri.user.application.mapper;

import kr.com.duri.groomer.domain.entity.Groomer;
import kr.com.duri.user.application.dto.request.NewReviewRequest;
import kr.com.duri.user.application.dto.response.ReviewResponse;
import kr.com.duri.user.domain.entity.Pet;
import kr.com.duri.user.domain.entity.Review;
import kr.com.duri.user.domain.entity.ReviewImage;

import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {

    // Review Entity to Response DTO
    public ReviewResponse toReviewResponse(
            Groomer groomer, Review review, ReviewImage reviewImage) {
        return ReviewResponse.builder()
                .reviewId(review.getId())
                .shopName(groomer.getShop().getName())
                .groomerName(groomer.getName())
                .rating(review.getRating())
                .comment(review.getComment())
                .imgUrl(reviewImage != null ? reviewImage.getImage() : null)
                .createdAt(review.getCreatedAt())
                .build();
    }

    // Review Request DTO to Entity
    public Review toReview(Pet pet, Groomer groomer, NewReviewRequest newReviewRequest) {
        if (groomer == null || pet == null) {
            throw new IllegalArgumentException("미용사 또는 펫의 정보가 공백입니다.");
        }
        return Review.builder()
                .shop(groomer.getShop())
                .pet(pet)
                .rating(newReviewRequest.getRating())
                .comment(newReviewRequest.getComment())
                .build();
    }
}
