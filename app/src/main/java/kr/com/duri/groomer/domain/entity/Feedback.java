package kr.com.duri.groomer.domain.entity;

import jakarta.persistence.*;
import kr.com.duri.common.entity.BaseEntity;
import kr.com.duri.groomer.domain.Enum.Behavior;
import kr.com.duri.groomer.domain.Enum.Friendly;
import kr.com.duri.groomer.domain.Enum.Reaction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "feedback")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Feedback extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feedback_id")
    private Long id; // 피드백 ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "groomer_id")
    private Groomer groomer; // 미용사 ID (FK)

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quotation_id")
    private Quotation quotation; // 견적서 ID (FK)

    @Enumerated(EnumType.STRING)
    @Column(name = "friendly")
    private Friendly friendly; // 미용사와의 친화력 (상(H), 중(M), 하(L))

    @Enumerated(EnumType.STRING)
    @Column(name = "reaction")
    private Reaction reaction; // 미용도구 반응 (공격적 행동, 도구를 피함, 거부반응 없음)

    @Enumerated(EnumType.STRING)
    @Column(name = "behavior")
    private Behavior behavior; // 반려견 행동 (미용중 경험한 내용/주의사항)

    @Column(name = "notice_content")
    private String noticeContent; // 사용자에게 전달되는 내용

    @Column(name = "portfolio_content")
    private String portfolioContent; // 포트폴리오 문구

    @Column(name = "expose")
    private Boolean expose; // 포트폴리오 노출 여부 (T: 노출, F: 비노출)

    @Column(name = "deleted")
    private Boolean deleted; // 논리 삭제 여부 (T: 삭제, F: 비삭제)

    public static Feedback createNewFeedback(
            Quotation quotation,
            Groomer groomer,
            String friendly,
            String reaction,
            String behavior,
            String noticeContent,
            String portfolioContent,
            Boolean expose) {
        return Feedback.builder()
                .quotation(quotation)
                .groomer(groomer)
                .friendly(Friendly.fromDescription(friendly))
                .reaction(Reaction.fromDescription(reaction))
                .behavior(Behavior.fromDescription(behavior))
                .noticeContent(noticeContent)
                .portfolioContent(portfolioContent)
                .expose(expose)
                .deleted(false)
                .build();
    }

    public Feedback removed() {
        this.deleted = true;
        return this;
    }
}
