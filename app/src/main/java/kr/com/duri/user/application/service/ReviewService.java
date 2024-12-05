package kr.com.duri.user.application.service;

import java.util.List;

import kr.com.duri.user.application.dto.request.UpdateReviewRequest;
import kr.com.duri.user.domain.entity.Review;

public interface ReviewService {

    // 매장 리뷰 조회
    List<Review> getReviewsByShopId(Long shopId);

    // 목록 조회
    List<Review> getReviewList(Long petId);

    // 단일 조회
    Review getReview(Long reviewId);

    // 추가
    Review createReview(Review review);

    // 수정
    Review updateReview(Long reviewId, UpdateReviewRequest newReviewRequest);

    // 삭제
    void deleteReview(Long reviewId);
}
