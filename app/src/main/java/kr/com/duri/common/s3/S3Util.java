package kr.com.duri.common.s3;

import java.io.IOException;
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
    private static final String S3_FOLDER_NAME = "review";

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

    public void deleteFromS3(String imageUrl) {
        String deleteS3Key = imageUrl.substring(imageUrl.indexOf(".com/") + 5);
        amazonS3.deleteObject(new DeleteObjectRequest(bucket, deleteS3Key));
    }
}
