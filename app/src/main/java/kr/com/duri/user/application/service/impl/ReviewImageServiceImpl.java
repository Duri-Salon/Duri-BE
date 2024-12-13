package kr.com.duri.user.application.service.impl;

import java.util.List;

import kr.com.duri.common.s3.S3Util;
import kr.com.duri.user.application.service.ReviewImageService;
import kr.com.duri.user.domain.entity.Review;
import kr.com.duri.user.domain.entity.ReviewImage;
import kr.com.duri.user.repository.ReviewImageRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;

@Service
@RequiredArgsConstructor
public class ReviewImageServiceImpl implements ReviewImageService {

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3;
    private static final String S3_BASIC_URL = "https://%s.s3.%s.amazonaws.com/%s";
    private static final String S3_FOLDER_NAME = "review";

    private final ReviewImageRepository reviewImageRepository;
    private final S3Util s3Util;

    // 목록 조회
    @Override
    public List<ReviewImage> getReviewImageList(Long reviewId) {
        return reviewImageRepository.findByReviewId(reviewId);
    }

    // 단일 조회
    @Override
    public ReviewImage getReviewImageByReviewId(Long reviewId) {
        return getReviewImageList(reviewId).stream().findFirst().orElse(new ReviewImage());
    }

    // 추가, 수정
    @Override
    public Boolean saveReviewImage(MultipartFile image, Review review) {
        if (image == null) { // 업로드 이미지 없을 경우
            return false;
        }
        // 1) 리뷰 이미지 S3 업로드
        String imageS3Url = s3Util.uploadToS3(image, "review");
        // 2) 기존 리뷰 이미지 있다면 S3 삭제 후 수정
        ReviewImage reviewImage = getReviewImageByReviewId(review.getId());
        if (reviewImage.getId() != null) {
            s3Util.deleteFromS3ByReviewImage(reviewImage);
            reviewImage.update(imageS3Url, review);
        } else { // 기존 이미지가 없다면 새로 생성
            reviewImage = ReviewImage.create(imageS3Url, review);
        }
        reviewImageRepository.save(reviewImage); // 저장
        return true;
    }

    // 삭제
    @Override
    public Boolean deleteReview(Long reviewId) {
        // 1) 조회
        ReviewImage reviewImage = getReviewImageByReviewId(reviewId);
        if (reviewImage == null) { // 사진 없음
            return false;
        }
        // 2) 삭제
        s3Util.deleteFromS3ByReviewImage(reviewImage); // S3 삭제
        reviewImageRepository.delete(reviewImage); // DB 삭제
        return true;
    }
}
