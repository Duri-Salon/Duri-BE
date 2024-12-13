package kr.com.duri.user.application.service;

import java.util.List;

import kr.com.duri.user.domain.entity.Review;
import kr.com.duri.user.domain.entity.ReviewImage;

import org.springframework.web.multipart.MultipartFile;

public interface ReviewImageService {

    // 목록 조회
    List<ReviewImage> getReviewImageList(Long reviewId);

    // 단일 조회
    ReviewImage getReviewImageByReviewId(Long reviewId);

    // 추가, 수정
    Boolean saveReviewImage(MultipartFile image, Review review);

    // 삭제
    Boolean deleteReview(Long reviewId);
}
