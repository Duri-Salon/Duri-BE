package kr.com.duri.user.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import kr.com.duri.common.entity.BaseEntity;
import kr.com.duri.groomer.domain.entity.Shop;
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

    @NotBlank
    @ManyToOne
    @JoinColumn(name = "shop_id")
    private Shop shop; // 매장 ID (FK)

    @NotBlank
    @ManyToOne
    @JoinColumn(name = "pet_id")
    private Pet pet; // 반려견 ID (FK)

    @NotBlank
    @Column(name = "rating")
    private Integer rating; // 별점 (0점~5점)

    @NotBlank
    @Column(name = "comment", columnDefinition = "TEXT")
    private String comment; // 리뷰 내용
}
