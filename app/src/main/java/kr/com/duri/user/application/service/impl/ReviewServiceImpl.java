package kr.com.duri.user.application.service.impl;

import java.util.List;

import kr.com.duri.groomer.exception.QuotationExistsException;
import kr.com.duri.user.application.dto.request.UpdateReviewRequest;
import kr.com.duri.user.application.service.ReviewService;
import kr.com.duri.user.domain.entity.Review;
import kr.com.duri.user.exception.ReviewNotFoundException;
import kr.com.duri.user.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    // 매장 리뷰 조회
    @Override
    public List<Review> getReviewsByShopId(Long shopId) {
        return reviewRepository.findAllByShopId(shopId);
    }

    // 내가 쓴 후기 목록 조회
    @Override
    public List<Review> getReviewsByPetId(Long petId) {
        return reviewRepository.findByPetId(petId);
    }

    // 단일 조회
    @Override
    public Review getReview(Long reviewId) {
        return reviewRepository
                .findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException("해당 리뷰를 찾을 수 없습니다."));
    }

    // 추가
    @Override
    public Review createReview(Review review) {
        boolean existRequest = reviewRepository.existsByRequestId(review.getRequest().getId());
        // 해당 견적에 리뷰가 이미 저장된 경우 작성 불가
        if (existRequest) {
            throw new QuotationExistsException("해당 요청 ID에 대한 리뷰가 이미 존재합니다.");
        }
        return reviewRepository.save(review);
    }

    // 수정
    @Override
    public Review updateReview(Long reviewId, UpdateReviewRequest newReviewRequest) {
        Review review = getReview(reviewId);
        review.update(newReviewRequest.getRating(), newReviewRequest.getComment());
        return reviewRepository.save(review);
    }

    // 삭제
    @Override
    public void deleteReview(Long reviewId) {
        Review review = getReview(reviewId);
        reviewRepository.deleteById(review.getId());
    }

    @Override
    public Review findByRequestId(Long requestId) {
        return reviewRepository.findByRequestId(requestId);
    }
}
