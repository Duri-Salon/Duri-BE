package kr.com.duri.groomer.application.service.impl;

import java.util.List;

import kr.com.duri.common.s3.S3Util;
import kr.com.duri.groomer.application.service.FeedbackImageService;
import kr.com.duri.groomer.domain.entity.Feedback;
import kr.com.duri.groomer.domain.entity.FeedbackImage;
import kr.com.duri.groomer.repository.FeedbackImageRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class FeedbackImageServiceImpl implements FeedbackImageService {

    private final FeedbackImageRepository feedbackImageRepository;

    private final S3Util s3Util;

    @Override
    public void saveFeedbackImages(Feedback feedback, List<MultipartFile> images) {
        if (images == null || images.isEmpty()) {
            return;
        }

        images.forEach(
                image -> {
                    String imageUrl = s3Util.uploadToS3(image, "feedback");
                    feedbackImageRepository.save(
                            FeedbackImage.createNewFeedbackImage(feedback, imageUrl));
                });
    }

    @Override
    public FeedbackImage findFirstFeedbackImageByFeedback(Long feedbackId) {
        return feedbackImageRepository
                .findFeedbackImagesByFeedbackIdOrderByCreatedAt(feedbackId)
                .stream()
                .map(feedbackImage -> feedbackImage)
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<String> findFeedbackImagesByFeedback(Feedback feedback) {
        List<FeedbackImage> feedbackImages =
                feedbackImageRepository.findFeedbackImagesByFeedback(feedback);
        return feedbackImages != null
                ? feedbackImages.stream().map(FeedbackImage::getImageUrl).toList()
                : List.of();
    }
}
