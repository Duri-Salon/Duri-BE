package kr.com.duri.groomer.repository;

import kr.com.duri.groomer.domain.entity.Feedback;
import kr.com.duri.groomer.domain.entity.FeedbackImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedbackImageRepository extends JpaRepository<FeedbackImage, Long> {
    List<FeedbackImage> findFeedbackImagesByFeedback(Feedback feedback);

    List<FeedbackImage> findFeedbackImagesByFeedbackIdOrderByCreatedAt(Long feedbackId);
}
