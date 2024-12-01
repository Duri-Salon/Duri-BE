package kr.com.duri.user.application.service;

import java.io.IOException;
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

    // S3 업로드
    String uploadToS3(MultipartFile image) throws IOException;

    // S3 삭제
    void deleteFromS3(ReviewImage reviewImage);

    // 삭제
    Boolean deleteReview(Long reviewId);
}
