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
@Table(name = "review_img")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewImage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_img_id")
    private Long id; // 리뷰이미지 ID

    @ManyToOne
    @JoinColumn(name = "review_id")
    private Review review; // 리뷰 ID (FK)

    @Column(name = "image")
    private String image; // 리뷰이미지 URL

    // 생성
    public static ReviewImage create(String imageUrl, Review review) {
        return ReviewImage.builder().image(imageUrl).review(review).build();
    }

    // 수정
    public void update(String imageUrl, Review review) {
        this.image = imageUrl;
        this.review = review;
        this.setUpdatedAt(LocalDateTime.now());
    }
}
