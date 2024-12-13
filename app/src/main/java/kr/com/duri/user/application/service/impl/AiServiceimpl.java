package kr.com.duri.user.application.service.impl;

import java.io.IOException;
import java.util.Map;

import kr.com.duri.user.application.service.AiService;
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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@RequiredArgsConstructor
public class AiServiceimpl implements AiService {

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

        // 1) HTTP 클라이언트를 사용하여 Replicate API 호출
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost request = new HttpPost("https://api.replicate.com/v1/predictions");
        // 2) API에게 보낼 Request Body (Prompt Hyper Parameter) 설정
        String json =
                new ObjectMapper()
                        .writeValueAsString(
                                Map.of(
                                        "version",
                                        version,
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
        // 3) 헤더 추가
        request.setHeader("Authorization", "Bearer " + replicateApiKey);
        request.setHeader("Content-Type", "application/json");
        request.setHeader("Prefer", "wait");
        // 4) 요청 및 응답
        try (CloseableHttpResponse response = httpClient.execute(request)) {
            if (response.getCode() == HttpStatus.SC_ACCEPTED
                    || response.getCode() == HttpStatus.SC_CREATED) {
                // 5) API 응답에서 결과 이미지 URL 파싱
                JsonNode responseBody =
                        new ObjectMapper().readTree(response.getEntity().getContent());
                String statusUrl = responseBody.get("urls").get("get").asText();
                // 6) 계속 상태 확인
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
                            // 7) 작업 성공: 결과 URL 반환
                            return statusBody.get("output").get(0).asText();
                        } else if ("failed".equals(status)) {
                            // 8) 작업 실패 처리
                            throw new RuntimeException(
                                    "Replicate API 응답 실패:"
                                            + statusBody.get("error").asText()
                                            + body);
                        }
                        // 9) 일정 시간 대기 후 다시 조회
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
}
