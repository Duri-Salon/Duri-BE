package kr.com.duri.user.application.service;

import java.util.List;

import kr.com.duri.groomer.domain.entity.Groomer;
import kr.com.duri.user.application.dto.request.NewReviewRequest;
import kr.com.duri.user.application.dto.request.UpdateReviewRequest;
import kr.com.duri.user.application.mapper.ReviewMapper;
import kr.com.duri.user.domain.entity.Pet;
import kr.com.duri.user.domain.entity.Review;
import kr.com.duri.user.exception.ReviewNotFoundException;
import kr.com.duri.user.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;

    // [1] 목록 조회
    @Override
    public List<Review> getReviewList(Long petId) {
        return reviewRepository.findByPetId(petId);
    }

    // [2] 단일 조회
    @Override
    public Review getReview(Long reviewId) {
        return reviewRepository
                .findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException("해당 리뷰를 찾을 수 없습니다."));
    }

    // [3] 추가
    @Override
    public Review createReview(Pet pet, Groomer groomer, NewReviewRequest newReviewRequest) { // ) {
        Review review = reviewMapper.toReview(pet, groomer, newReviewRequest);
        return reviewRepository.save(review);
    }

    // [4] 수정
    @Override
    public Review updateReview(Long reviewId, UpdateReviewRequest newReviewRequest) { // ) {
        Review review = getReview(reviewId);
        review.update(newReviewRequest.getRating(), newReviewRequest.getComment());
        return reviewRepository.save(review);
    }

    // [5] 삭제
    @Override
    public void deleteReview(Long reviewId) {
        reviewRepository.deleteById(reviewId);
    }
}
