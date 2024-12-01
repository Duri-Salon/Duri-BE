package kr.com.duri.user.application.service;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import kr.com.duri.user.domain.entity.Review;
import kr.com.duri.user.domain.entity.ReviewImage;
import kr.com.duri.user.exception.ReviewImageUploadException;
import kr.com.duri.user.repository.ReviewImageRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

@Service
@RequiredArgsConstructor
public class ReviewImageServiceImpl implements ReviewImageService {

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3;
    private static final String S3_BASIC_URL = "https://%s.s3.%s.amazonaws.com/%s";
    private static final String S3_FOLDER_NAME = "review";

    private final ReviewImageRepository reviewImageRepository;

    // [1] 목록 조회
    @Override
    public List<ReviewImage> getReviewImageList(Long reviewId) {
        return reviewImageRepository.findByReviewId(reviewId);
    }

    // [1-1] 단일 조회
    @Override
    public ReviewImage getReviewImageByReviewId(Long reviewId) {
        return getReviewImageList(reviewId).stream().findFirst().orElse(null);
    }

    // [2] 추가, 수정
    @Override
    public Boolean saveReviewImage(MultipartFile image, Review review) {
        // 업로드 이미지 없을 경우
        if (image == null) {
            return false;
            // throw new RuntimeException("등록된 이미지가 없습니다.");
        }
        // 리뷰 이미지 S3 업로드
        String imageS3Url = uploadToS3(image);
        // 기존 리뷰 이미지 있다면 S3 삭제 후 수정
        ReviewImage reviewImage = getReviewImageByReviewId(review.getId());
        if (reviewImage != null) {
            deleteFromS3(reviewImage);
            reviewImage.update(imageS3Url, review);
        } else {
            // 기존 이미지가 없다면 새로 생성
            reviewImage = ReviewImage.create(imageS3Url, review);
        }
        reviewImageRepository.save(reviewImage); // 저장
        return true;
    }

    // [2-1] S3 업로드
    public String uploadToS3(MultipartFile image) {
        try {
            // 고유 파일명
            String fileName =
                    S3_FOLDER_NAME
                            + "/"
                            + UUID.randomUUID()
                            + "_"
                            + image.getOriginalFilename(); // 폴더 내부에 랜덤숫자_원본파일명
            // 메타 데이터
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(image.getContentType());
            metadata.setContentLength(image.getSize());
            // S3에 파일 업로드
            PutObjectRequest putObjectRequest =
                    new PutObjectRequest(bucket, fileName, image.getInputStream(), metadata);
            amazonS3.putObject(putObjectRequest);
            return String.format(S3_BASIC_URL, bucket, amazonS3.getRegionName(), fileName);
        } catch (IOException e) {
            throw new ReviewImageUploadException("해당 이미지를 불러오는 데 실패하였습니다.");
        } catch (Exception e) {
            throw new ReviewImageUploadException("해당 이미지를 S3에 업로드하는 데 실패하였습니다.");
        }
    }

    // [2-2] S3 삭제
    public void deleteFromS3(ReviewImage reviewImage) {
        String originS3Url = reviewImage.getImage();
        String deleteS3Key = originS3Url.substring(originS3Url.indexOf(".com/") + 5);
        amazonS3.deleteObject(new DeleteObjectRequest(bucket, deleteS3Key));
    }

    // [3] 삭제
    @Override
    public Boolean deleteReview(Long reviewId) {
        // 조회
        ReviewImage reviewImage = getReviewImageByReviewId(reviewId);
        if (reviewImage == null) { // 사진 없음
            return false;
        }
        // 삭제
        deleteFromS3(reviewImage); // S3 삭제
        reviewImageRepository.delete(reviewImage); // DB 삭제
        return true;
    }
}
