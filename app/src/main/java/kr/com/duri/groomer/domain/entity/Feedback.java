package kr.com.duri.groomer.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import kr.com.duri.common.entity.BaseEntity;
import kr.com.duri.groomer.domain.Enum.Friendly;
import kr.com.duri.user.domain.entity.Pet;
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

    @NotBlank
    @ManyToOne
    @JoinColumn(name = "groomer_id")
    private Groomer groomer; // 미용사 ID (FK)

    @NotBlank
    @ManyToOne
    @JoinColumn(name = "pet_id")
    private Pet pet; // 반려견 ID (FK)

    @Column(name = "content")
    private String content; // 한줄평

    @Column(name = "friendly")
    @Enumerated(EnumType.STRING)
    private Friendly friendly; // 미용사와의 친화력 (상(H), 중(M), 하(L))

    @Column(name = "reaction")
    private String reaction; // 미용도구 반응 (공격적 행동, 도구를 피함, 거부반응 없음)

    @Column(name = "action")
    private String action; // 반려견 행동 (미용중 경험한 내용/주의사항)

    @Column(name = "image")
    private String image; // 피드백 이미지 URL

    @NotBlank
    @Column(name = "expose")
    private Boolean expose; // 미용사 노출 여부 (T: 노출, F: 비노출)
}
