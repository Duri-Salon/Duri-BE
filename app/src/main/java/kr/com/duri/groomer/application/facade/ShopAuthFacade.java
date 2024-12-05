package kr.com.duri.groomer.application.facade;

import kr.com.duri.common.security.jwt.JwtUtil;
import kr.com.duri.groomer.application.dto.response.NewShopJwtResponse;
import kr.com.duri.groomer.application.mapper.ShopJwtMapper;
import kr.com.duri.groomer.application.service.ShopService;
import kr.com.duri.groomer.domain.entity.Shop;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ShopAuthFacade {

    private final ShopService shopService;

    private final JwtUtil jwtUtil;

    private final ShopJwtMapper shopJwtMapper;

    public NewShopJwtResponse createNewShopJwt(String providerId) {
        Shop shop =
                shopService
                        .findBySocialId(providerId)
                        .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        String token = jwtUtil.createJwt(shop.getId(), providerId, 60 * 60 * 60 * 60L);

        return shopJwtMapper.toNewJwtResponse("authorization_shop", token, shop.getNewShop());
    }
}
