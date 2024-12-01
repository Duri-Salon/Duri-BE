package kr.com.duri.user.application.service;

import java.util.List;

import kr.com.duri.groomer.domain.entity.Groomer;
import kr.com.duri.user.application.dto.request.NewReviewRequest;
import kr.com.duri.user.application.dto.request.UpdateReviewRequest;
import kr.com.duri.user.domain.entity.Pet;
import kr.com.duri.user.domain.entity.Review;

public interface ReviewService {

    // 목록 조회
    List<Review> getReviewList(Long petId);

    // 단일 조회
    Review getReview(Long reviewId);

    // 추가
    Review createReview(Pet pet, Groomer groomer, NewReviewRequest newReviewRequest); // );

    // 수정
    Review updateReview(Long reviewId, UpdateReviewRequest newReviewRequest); // );

    // 삭제
    void deleteReview(Long reviewId);
}
