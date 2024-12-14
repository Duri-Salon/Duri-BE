package kr.com.duri.groomer.application.service;

import kr.com.duri.groomer.domain.entity.Feedback;
import kr.com.duri.groomer.domain.entity.FeedbackImage;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FeedbackImageService {
    void saveFeedbackImages(Feedback feedback, List<MultipartFile> images);

    FeedbackImage findFirstFeedbackImageByFeedback(Long feedbackId);

    List<String> findFeedbackImagesByFeedback(Feedback feedback);
}
