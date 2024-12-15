package kr.com.duri.groomer.domain.entity;

import jakarta.persistence.*;
import kr.com.duri.common.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "feedback_image")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedbackImage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long id; // 이미지 ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feedback_id")
    private Feedback feedback; // 피드백 ID (FK)

    @Column(name = "image_url")
    private String imageUrl; // 이미지 URL

    public static FeedbackImage createNewFeedbackImage(Feedback feedback, String imageUrl) {
        return FeedbackImage.builder().feedback(feedback).imageUrl(imageUrl).build();
    }
}
