 package kr.com.duri.groomer.application.facade;

 import kr.com.duri.groomer.application.dto.request.ShopOnboardingRequest;
 import kr.com.duri.groomer.application.dto.request.ShopProfileDetailRequest;
 import kr.com.duri.groomer.application.dto.response.GroomerProfileDetailResponse;
 import kr.com.duri.groomer.application.dto.response.ShopDetailResponse;
 import kr.com.duri.groomer.application.dto.response.ShopOnboardingResponse;
 import kr.com.duri.groomer.application.dto.response.ShopProfileDetailResponse;
 import kr.com.duri.groomer.application.mapper.GroomerMapper;
 import kr.com.duri.groomer.application.mapper.ShopMapper;
 import kr.com.duri.groomer.application.service.GroomerService;
 import kr.com.duri.groomer.application.service.ShopImageService;
 import kr.com.duri.groomer.application.service.ShopService;
 import kr.com.duri.groomer.domain.entity.Groomer;
 import kr.com.duri.groomer.domain.entity.Shop;
 import lombok.RequiredArgsConstructor;

 import org.springframework.stereotype.Component;
 import org.springframework.web.multipart.MultipartFile;

 import java.util.List;

 @Component
 @RequiredArgsConstructor
 public class ShopProfileFacade {

    private final ShopService shopService;

    private final ShopImageService shopImageService;

    private final GroomerService groomerService;

    private final ShopMapper shopMapper;

    private final GroomerMapper groomerMapper;

    public ShopOnboardingResponse shopAndGroomerOnboarding(
            String token, ShopOnboardingRequest shopOnboardingRequest) {
        Long shopId = shopService.getShopIdByToken(token);

        Shop shop = shopService.findById(shopId);

        shop = shopService.updateDetail(shop, shopOnboardingRequest.getShopOnboardingInfo());

        Groomer groomer =
                groomerService.createNewGroomer(
                        shop, shopOnboardingRequest.getGroomerOnboardingInfo());

        return ShopOnboardingResponse.builder()
                .shopDetailResponse(shopMapper.toShopDetailResponse(shop))
                .groomerDetailResponse(groomerMapper.toGroomerDetailResponse(groomer))
                .build();
    }

    public ShopProfileDetailResponse updateShopProfile(String token, ShopProfileDetailRequest
 shopProfileDetailRequest, MultipartFile img) {
        Long shopId = shopService.getShopIdByToken(token);
        Shop shop = shopService.findById(shopId);
        String imageUrl = shopImageService.uploadShopMainImage(shop, img);

        shop = shopService.updateDetail(shop, shopProfileDetailRequest);

        return shopMapper.toShopProfileDetailResponse(shop, imageUrl);
    }

     public List<GroomerProfileDetailResponse> getShopGroomerList(String token) {
     }
 }
