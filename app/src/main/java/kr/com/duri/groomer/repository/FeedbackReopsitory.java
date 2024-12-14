package kr.com.duri.groomer.repository;

import kr.com.duri.groomer.domain.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedbackReopsitory extends JpaRepository<Feedback, Long> {
    List<Feedback> findByGroomerIdAndExposeTrueAndDeletedFalseOrderByCreatedAtDesc(Long groomerId);
}
