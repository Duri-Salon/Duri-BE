package kr.com.duri.user.repository;

import java.util.List;

import kr.com.duri.user.domain.entity.ReviewImage;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewImageRepository extends JpaRepository<ReviewImage, Long> {

    // 리뷰 ID 기준 리뷰 이미지 찾기
    List<ReviewImage> findByReviewId(Long reviewId);
}
