package kr.com.duri.groomer.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import kr.com.duri.common.entity.BaseEntity;
import kr.com.duri.groomer.domain.Enum.ImageCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "shop_image")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShopImage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shop_image_id")
    private Long id; // 매장 이미지 ID

    @NotBlank
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id")
    private Shop shop; // 매장 ID FK

    @Column(name = "shop_image_url")
    private String shopImageUrl; // 매장 이미지 URL

    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private ImageCategory category; // 이미지 카테고리
}
