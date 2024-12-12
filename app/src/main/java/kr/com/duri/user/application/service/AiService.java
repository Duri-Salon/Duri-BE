package kr.com.duri.user.application.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public interface AiService {
    String uploadS3(MultipartFile image);

    String generatePrompt(String styleText);

    String generateNegativePrompt(String styleText);

    String callReplicateApi(String imageUrl, String prompt, String negativePrompt, String version)
            throws IOException;

    void deleteFromS3(String imageURL);

    String generateModel(String styleText);
}
