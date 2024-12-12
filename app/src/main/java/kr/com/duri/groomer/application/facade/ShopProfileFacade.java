package kr.com.duri.groomer.application.facade;

import java.util.List;

import kr.com.duri.common.exception.IllegalParameterException;
import kr.com.duri.groomer.application.dto.request.ShopOnboardingRequest;
import kr.com.duri.groomer.application.dto.request.ShopProfileDetailRequest;
import kr.com.duri.groomer.application.dto.response.GroomerProfileDetailResponse;
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

    public ShopProfileDetailResponse updateShopProfile(
            String token, ShopProfileDetailRequest shopProfileDetailRequest, MultipartFile img) {
        Long shopId = shopService.getShopIdByToken(token);
        Shop shop = shopService.findById(shopId);
        shop = shopService.updateDetail(shop, shopProfileDetailRequest);
        if (img == null || img.isEmpty()) {
            if (shopImageService.existMainImage(shop)) {
                String existingImageUrl = shopImageService.getMainShopImage(shop).getShopImageUrl();
                return shopMapper.toShopProfileDetailResponse(shop, existingImageUrl);
            } else {
                return shopMapper.toShopProfileDetailResponse(shop, null);
            }
        }
        if (shopImageService.existMainImage(shop)) {
            shopImageService.deleteShopMainImage(shop);
        }
        String newImageUrl = shopImageService.uploadShopMainImage(shop, img);
        return shopMapper.toShopProfileDetailResponse(shop, newImageUrl);
    }

    public List<GroomerProfileDetailResponse> getShopGroomerList(String token) {
        Long shopId = shopService.getShopIdByToken(token);
        List<Groomer> groomers = groomerService.findGroomersByShop(shopId);
        return groomerMapper.toGroomerProfileDetailResponseList(groomers);
    }

    public void insertNewShopImage(String token, List<MultipartFile> images) {
        if (images == null || images.isEmpty()) {
            throw new IllegalParameterException("이미지가 없습니다.");
        }
        Long shopId = shopService.getShopIdByToken(token);
        Shop shop = shopService.findById(shopId);
        shopImageService.uploadShopImages(shop, images);
    }

    public List<String> getShopImageList(String token) {
        Long shopId = shopService.getShopIdByToken(token);
        Shop shop = shopService.findById(shopId);
        return shopImageService.findImagesByShop(shop);
    }
}
