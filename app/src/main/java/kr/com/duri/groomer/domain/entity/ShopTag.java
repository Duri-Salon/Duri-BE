package kr.com.duri.groomer.domain.entity;

import jakarta.persistence.*;
import kr.com.duri.common.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "shop_tag")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShopTag extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id")
    private Long id; // 태그 ID

    @Column(name = "shop_id")
    private Long shopId; // 매장 ID FK

    @Column(name = "tag_name")
    private String tagName; // 태그명

    public static ShopTag insertShopTag(Long shopId, String tagName) {
        return ShopTag.builder().shopId(shopId).tagName(tagName).build();
    }
}
