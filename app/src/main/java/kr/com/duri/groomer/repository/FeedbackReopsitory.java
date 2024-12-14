package kr.com.duri.groomer.repository;

import kr.com.duri.groomer.domain.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackReopsitory extends JpaRepository<Feedback, Long> {
}
