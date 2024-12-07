package kr.com.duri.groomer.application.facade;

import kr.com.duri.groomer.application.dto.request.ShopOnboardingRequest;
import kr.com.duri.groomer.application.dto.response.ShopOnboardingResponse;
import kr.com.duri.groomer.application.mapper.GroomerMapper;
import kr.com.duri.groomer.application.mapper.ShopMapper;
import kr.com.duri.groomer.application.service.GroomerService;
import kr.com.duri.groomer.application.service.ShopService;
import kr.com.duri.groomer.domain.entity.Groomer;
import kr.com.duri.groomer.domain.entity.Shop;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ShopOnboardingFacade {

    private final ShopService shopService;

    private final GroomerService groomerService;

    private final ShopMapper shopMapper;

    private final GroomerMapper groomerMapper;

    public ShopOnboardingResponse shopAndGroomerOnboarding(
            String token, ShopOnboardingRequest shopOnboardingRequest) {
        Long shopId = shopService.getShopIdByToken(token);

        Shop shop = shopService.findById(shopId);

        shop = shopService.updateDetail(shop, shopOnboardingRequest.getShopDetailRequest());

        Groomer groomer =
                groomerService.createNewGroomer(
                        shop, shopOnboardingRequest.getGroomerDetailRequest());

        return ShopOnboardingResponse.builder()
                .shopDetailResponse(shopMapper.toShopDetailResponse(shop))
                .groomerDetailResponse(groomerMapper.toGroomerDetailResponse(groomer))
                .build();
    }
}
