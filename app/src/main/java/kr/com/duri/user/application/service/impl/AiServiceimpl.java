package kr.com.duri.user.application.service.impl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import kr.com.duri.user.application.service.AiService;
import kr.com.duri.user.exception.ReviewImageUploadException;
import lombok.RequiredArgsConstructor;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@RequiredArgsConstructor
public class AiServiceimpl implements AiService {

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3;
    private static final String S3_BASIC_URL = "https://%s.s3.%s.amazonaws.com/%s";
    private static final String S3_FOLDER_NAME = "ai";

    @Value("${replicate.api.token}")
    private String replicateApiKey;

    // 스타일별 프롬프트
    private static final Map<String, String> STYLE_PROMPTS =
            Map.of(
                    "테디베어",
                    """
For a "Teddy bear cut", trim the dog's ear hair into a perfectly round, fluffy shape, resembling a teddy bear's ears, while keeping the face, eyes, nose, and mouth intact. You need to know. Your pet's face should not have a teddy bear feel. You just need to change the shape of the ears to bear ears.
""",
                    "베이비",
                    """
For a "Baby cut", dog's facial hair is trimmed to a very short length, giving the face a very round, baby-like appearance.
""",
                    "라이언",
                    """
For a "Lion cut", cut short the body fur, leaving the chest and neck fur longer. Create neck (including chin) fur as a majestic mane similar to that of a lion, while keeping the face, eyes, nose, and mouth intact.
""");

    // 스타일별 네거티브 프롬프트
    private static final Map<String, String> STYLE_NEGATIVE_PROMPTS =
            Map.of(
                    "테디베어", "Synthetic of dog's face with bear face",
                    "베이비", "Synthetic of dog's face with bear face",
                    "라이언", "Synthetic of lion's face with bear face");

    // 스타일별 모델 선정
    private static final Map<String, String> STYLE_VERSIONS =
            Map.of(
                    // mcai/babes-v2.0-img2img
                    "테디베어", "2bca10ed539cf2196f18b4ec85128a80355d94934db8620884ecca552cdc4def",
                    // mcai/realistic-vision-v2.0-img2img
                    "베이비", "c7c4a7c1ed0baa97a4e9f12b76fa2377aad131504afd405b10abb91d0f1af899",
                    "라이언", "c7c4a7c1ed0baa97a4e9f12b76fa2377aad131504afd405b10abb91d0f1af899");

    // S3 사진 업로드
    public String uploadS3(MultipartFile image) {
        try {
            // 고유 파일명
            String fileName =
                    S3_FOLDER_NAME
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
            System.out.println(" ");
            return String.format(S3_BASIC_URL, bucket, amazonS3.getRegionName(), fileName);
        } catch (IOException e) {
            throw new ReviewImageUploadException("이미지 로딩 실패");
        } catch (Exception e) {
            throw new ReviewImageUploadException("이미지 S3 업로드 실패");
        }
    }

    // 프롬프트 생성
    public String generatePrompt(String styleText) {
        String stylePrompt = STYLE_PROMPTS.get(styleText);
        if (stylePrompt == null) {
            throw new RuntimeException("잘못된 스타일링 입력 값");
        }
        return """
        - Short and neat fur on the dog's body. Apply clean trimming to all visible fur. Fur length no longer than 0.5 cm.
        - Apply a specific grooming style to dog's coat.
    """
                + stylePrompt
                + """
        - Make sure the final image clearly highlights your intended grooming style.
        - At this time, the dog's face and overall structure must remain natural and realistic.
    """;
    }

    // 네거티브 프롬프트 생성
    public String generateNegativePrompt(String styleText) {
        String negativeStylePrompt = STYLE_NEGATIVE_PROMPTS.get(styleText);
        if (negativeStylePrompt == null) {
            throw new RuntimeException("잘못된 스타일링 입력 값");
        }
        return negativeStylePrompt
                + ", non-puppy appearance, longer then 0.5cm puppy's fur length, Deformed, cartoonish, oversaturated, blurry, bad anatomy, mutant, extra limbs, poorly drawn, watermarked, text, body out of frame, extra legs, extra arms, deformed ";
    }

    // 네거티브 프롬프트 생성
    public String generateModel(String styleText) {
        return STYLE_VERSIONS.get(styleText);
    }

    // Replicate API 호출
    public String callReplicateApi(
            String imageUrl, String prompt, String negativePrompt, String version)
            throws IOException {
        System.out.println("model : " + version);
        System.out.println("S3 업로드된 주소 : " + imageUrl);
        System.out.println("propmt : " + prompt);
        System.out.println("negative : " + negativePrompt);

        // HTTP 클라이언트를 사용하여 Replicate API 호출
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost request = new HttpPost("https://api.replicate.com/v1/predictions");
        // API에게 보낼 Request Body (Prompt Hyper Parameter) 설정
        String json =
                new ObjectMapper()
                        .writeValueAsString(
                                Map.of(
                                        "version",
                                        version,
                                        // "c7c4a7c1ed0baa97a4e9f12b76fa2377aad131504afd405b10abb91d0f1af899",
                                        "input",
                                        Map.of(
                                                "image",
                                                imageUrl,
                                                "prompt",
                                                prompt,
                                                "negative_prompt",
                                                negativePrompt,
                                                "strength",
                                                0.28,
                                                "upscale",
                                                2,
                                                "num_outputs",
                                                1,
                                                "num_inference_steps",
                                                30,
                                                "guidance_scale",
                                                13,
                                                "scheduler",
                                                "DDIM",
                                                "seed",
                                                55)));
        StringEntity entity = new StringEntity(json);
        request.setEntity(entity);
        // 헤더 추가
        request.setHeader("Authorization", "Bearer " + replicateApiKey);
        request.setHeader("Content-Type", "application/json");
        request.setHeader("Prefer", "wait");
        // 요청 및 응답 확인
        try (CloseableHttpResponse response = httpClient.execute(request)) {
            if (response.getCode() == HttpStatus.SC_ACCEPTED
                    || response.getCode() == HttpStatus.SC_CREATED) {
                // API 응답에서 결과 이미지 URL 파싱
                JsonNode responseBody =
                        new ObjectMapper().readTree(response.getEntity().getContent());
                String statusUrl = responseBody.get("urls").get("get").asText();
                // 계속 상태 확인
                while (true) {
                    HttpGet statusRequest = new HttpGet(statusUrl);
                    statusRequest.setHeader("Authorization", "Bearer " + replicateApiKey);

                    try (CloseableHttpResponse statusResponse = httpClient.execute(statusRequest)) {
                        JsonNode statusBody =
                                new ObjectMapper()
                                        .readTree(statusResponse.getEntity().getContent());
                        String status = statusBody.get("status").asText();
                        String body = statusBody.asText();

                        if ("succeeded".equals(status)) {
                            // 작업 성공: 결과 URL 반환
                            return statusBody.get("output").get(0).asText();
                        } else if ("failed".equals(status)) {
                            // 작업 실패 처리
                            throw new RuntimeException(
                                    "Replicate API 응답 실패:"
                                            + statusBody.get("error").asText()
                                            + body);
                        }
                        // 일정 시간 대기 후 다시 조회
                        Thread.sleep(2000);
                    }
                }
            } else {
                throw new RuntimeException("Replicate API 요청 실패:" + response.getReasonPhrase());
            }
        } catch (InterruptedException e) {
            throw new RuntimeException("상태 확인 중 인터럽트 발생");
        }
    }

    // S3 삭제
    public void deleteFromS3(String imageURL) {
        String deleteS3Key = imageURL.substring(imageURL.indexOf(".com/") + 5);
        amazonS3.deleteObject(new DeleteObjectRequest(bucket, deleteS3Key));
    }
}
