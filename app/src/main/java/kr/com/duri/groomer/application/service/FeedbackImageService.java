package kr.com.duri.groomer.application.service;

import java.util.List;

import kr.com.duri.groomer.domain.entity.Feedback;
import kr.com.duri.groomer.domain.entity.FeedbackImage;

import org.springframework.web.multipart.MultipartFile;

public interface FeedbackImageService {
    void saveFeedbackImages(Feedback feedback, List<MultipartFile> images);

    FeedbackImage findFirstFeedbackImageByFeedback(Long feedbackId);

    List<String> findFeedbackImagesByFeedback(Feedback feedback);
}
