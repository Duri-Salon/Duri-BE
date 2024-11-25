package kr.com.duri.groomer.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import kr.com.duri.common.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "feedback_condition")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedbackCondition extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "condition_id")
    private Long id; // 컨디션 ID

    @NotBlank
    @ManyToOne
    @JoinColumn(name = "feedback_id")
    private Feedback feedback; // 피드백 ID (FK)

    @Column(name = "condition_type")
    private String conditionType; // 스트레스/질환

    @Column(name = "content")
    private String content; // 내용 (피하려는 행동, 과도한 짖음 등)
}
