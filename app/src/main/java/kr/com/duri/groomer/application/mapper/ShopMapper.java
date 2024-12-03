package kr.com.duri.groomer.application.mapper;

import kr.com.duri.groomer.application.dto.response.ShopDetailResponse;
import kr.com.duri.groomer.domain.entity.Shop;
import kr.com.duri.groomer.domain.entity.ShopImage;

import org.springframework.stereotype.Component;

@Component
public class ShopMapper {

    // Shop Entity to ShopDetailResponse DTO
    public ShopDetailResponse toShopDetailResponse(Shop shop, ShopImage shopImage) {
        return ShopDetailResponse.builder()
                .id(shop.getId())
                .name(shop.getName())
                .address(shop.getAddress())
                .imageURL(shopImage.getShopImageUrl())
                .build();
    }
}
