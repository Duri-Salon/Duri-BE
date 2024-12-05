package kr.com.duri.user.domain.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import kr.com.duri.common.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "review")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id; // 리뷰 ID

    @OneToOne
    @JoinColumn(name = "request_id")
    private Request request; // 견적 요청 ID (FK)

    @Column(name = "rating")
    private Integer rating; // 별점 (0점~5점)

    @Column(name = "comment", columnDefinition = "TEXT")
    private String comment; // 리뷰

    // 수정
    public void update(int rating, String comment) {
        this.rating = rating;
        this.comment = comment;
        this.setUpdatedAt(LocalDateTime.now());
    }
}
