package kr.com.duri.user.application.service;

import java.io.IOException;

public interface AiService {

    String generatePrompt(String styleText);

    String generateNegativePrompt(String styleText);

    String callReplicateApi(String imageUrl, String prompt, String negativePrompt, String version)
            throws IOException;

    String generateModel(String styleText);
}
