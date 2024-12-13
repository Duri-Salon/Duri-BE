package kr.com.duri.common.s3;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

import kr.com.duri.user.domain.entity.ReviewImage;
import kr.com.duri.user.exception.ReviewImageUploadException;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

@Component
@RequiredArgsConstructor
public class S3Util {

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3;
    private static final String S3_BASIC_URL = "https://%s.s3.%s.amazonaws.com/%s";

    // S3 사진 업로드
    public String uploadToS3(MultipartFile image, String s3FolderName) {
        try {
            // 고유 파일명
            String fileName =
                    s3FolderName
                            + "/"
                            + UUID.randomUUID()
                            + "_"
                            + LocalDateTime.now(); // 폴더 내부에 랜덤숫자_원본파일명
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
            throw new ReviewImageUploadException("이미지 로딩 실패");
        } catch (Exception e) {
            throw new ReviewImageUploadException("이미지 S3 업로드 실패");
        }
    }

    // S3 삭제
    public void deleteFromS3(String imageURL) {
        String deleteS3Key = imageURL.substring(imageURL.indexOf(".com/") + 5);
        amazonS3.deleteObject(new DeleteObjectRequest(bucket, deleteS3Key));
    }
}
