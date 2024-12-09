package kr.com.duri.groomer.application.mapper;

import kr.com.duri.groomer.application.dto.response.NewShopJwtResponse;
import kr.com.duri.groomer.domain.entity.Shop;

import org.springframework.stereotype.Component;

@Component
public class ShopJwtMapper {

    public NewShopJwtResponse toNewJwtResponse(Shop shop, String token) {
        return NewShopJwtResponse.builder()
                .token(token)
                .client("authorization_shop")
                .newUser(shop.getNewShop())
                .entry(shop.getEntry())
                .build();
    }
}
