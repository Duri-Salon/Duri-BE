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
import kr.com.duri.groomer.application.service.ShopTagService;
import kr.com.duri.groomer.domain.entity.Groomer;
import kr.com.duri.groomer.domain.entity.Shop;
import kr.com.duri.groomer.domain.entity.ShopImage;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
public class ShopProfileFacade {

    private final ShopService shopService;

    private final ShopImageService shopImageService;

    private final ShopTagService shopTagService;

    private final GroomerService groomerService;

    private final ShopMapper shopMapper;

    private final GroomerMapper groomerMapper;

    public ShopOnboardingResponse shopAndGroomerOnboarding(
            String token, ShopOnboardingRequest shopOnboardingRequest, MultipartFile img) {
        Long shopId = shopService.getShopIdByToken(token);
        Shop shop = shopService.findById(shopId);
        shop = shopService.updateDetail(shop, shopOnboardingRequest.getShopOnboardingInfo());
        String groomerImageUrl = groomerService.uploadGroomerImage(img);
        Groomer groomer =
                groomerService.createNewGroomer(
                        shop, shopOnboardingRequest.getGroomerOnboardingInfo(), groomerImageUrl);

        return ShopOnboardingResponse.builder()
                .shopDetailResponse(shopMapper.toShopDetailResponse(shop))
                .groomerProfileDetailResponse(groomerMapper.toGroomerProfileDetailResponse(groomer))
                .build();
    }

    public ShopProfileDetailResponse updateShopProfile(
            String token, ShopProfileDetailRequest shopProfileDetailRequest) {
        Long shopId = shopService.getShopIdByToken(token);
        Shop shop = shopService.findById(shopId);
        shop = shopService.updateDetail(shop, shopProfileDetailRequest);
        shopTagService.removeAllTags(shop); // 기존에 있던 모든 태그를 삭제
        List<String> shopTags =
                shopTagService.updateShopTags(
                        shop, shopProfileDetailRequest.getTags()); // 새로운 태그를 추가 (최대 3개)
        ShopImage shopImage = shopImageService.getMainShopImage(shop);
        String imageUrl =
                shopImage == null || shopImage.getShopImageUrl() == null
                        ? null
                        : shopImage.getShopImageUrl();
        return shopMapper.toShopProfileDetailResponse(shop, imageUrl, shopTags);
    }

    public ShopProfileDetailResponse updateShopProfileImage(String token, MultipartFile img) {
        Long shopId = shopService.getShopIdByToken(token);
        Shop shop = shopService.findById(shopId);
        List<String> shopTags = shopTagService.findTagsByShopId(shop.getId());
        if (img == null || img.isEmpty()) {
            if (shopImageService.existMainImage(shop)) {
                String existingImageUrl = shopImageService.getMainShopImage(shop).getShopImageUrl();
                return shopMapper.toShopProfileDetailResponse(shop, existingImageUrl, shopTags);
            } else {
                return shopMapper.toShopProfileDetailResponse(shop, null, shopTags);
            }
        }
        if (shopImageService.existMainImage(shop)) {
            shopImageService.deleteShopMainImage(shop);
        }
        String newImageUrl = shopImageService.uploadShopMainImage(shop, img);
        return shopMapper.toShopProfileDetailResponse(shop, newImageUrl, shopTags);
    }

    public void insertNewShopImage(String token, List<MultipartFile> images) {
        if (images == null || images.isEmpty()) {
            throw new IllegalParameterException("이미지가 없습니다.");
        }
        Long shopId = shopService.getShopIdByToken(token);
        Shop shop = shopService.findById(shopId);
        shopImageService.uploadShopImages(shop, images);
    }

    public List<GroomerProfileDetailResponse> getShopGroomerList(String token) {
        Long shopId = shopService.getShopIdByToken(token);
        List<Groomer> groomers = groomerService.findGroomersByShop(shopId);
        return groomerMapper.toGroomerProfileDetailResponseList(groomers);
    }

    public List<GroomerProfileDetailResponse> getShopGroomerList(Long shopId) {
        List<Groomer> groomers = groomerService.findGroomersByShop(shopId);
        return groomerMapper.toGroomerProfileDetailResponseList(groomers);
    }

    public List<String> getShopImageList(String token) {
        Long shopId = shopService.getShopIdByToken(token);
        Shop shop = shopService.findById(shopId);
        return shopImageService.findImagesByShop(shop);
    }

    public List<String> getShopImageList(Long shopId) {
        Shop shop = shopService.findById(shopId);
        return shopImageService.findImagesByShop(shop);
    }
}
