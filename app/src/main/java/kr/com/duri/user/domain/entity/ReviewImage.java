package kr.com.duri.user.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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

    @NotBlank
    @ManyToOne
    @JoinColumn(name = "review_id")
    private Review review; // 리뷰 ID (FK)

    @NotBlank
    @Column(name = "image")
    private String image; // 리뷰이미지 URL
}
