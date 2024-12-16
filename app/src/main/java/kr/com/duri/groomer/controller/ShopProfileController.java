package kr.com.duri.groomer.controller;

import java.util.List;

import jakarta.validation.Valid;
import kr.com.duri.common.response.CommonResponseEntity;
import kr.com.duri.groomer.application.dto.request.ShopOnboardingRequest;
import kr.com.duri.groomer.application.dto.request.ShopProfileDetailRequest;
import kr.com.duri.groomer.application.dto.response.GroomerProfileDetailResponse;
import kr.com.duri.groomer.application.dto.response.ShopOnboardingResponse;
import kr.com.duri.groomer.application.dto.response.ShopProfileDetailResponse;
import kr.com.duri.groomer.application.facade.ShopProfileFacade;
import lombok.RequiredArgsConstructor;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/shop")
public class ShopProfileController {

    private final ShopProfileFacade shopProfileFacade;

    @PostMapping(value = "/onboarding", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CommonResponseEntity<ShopOnboardingResponse> shopAndGroomerOnboarding(
            @RequestHeader("authorization_shop") String token,
            @RequestPart @Valid ShopOnboardingRequest shopOnboardingRequest,
            @RequestPart(value = "image", required = false) MultipartFile img) {
        return CommonResponseEntity.success(
                shopProfileFacade.shopAndGroomerOnboarding(token, shopOnboardingRequest, img));
    }

    @PutMapping("/profile")
    public CommonResponseEntity<ShopProfileDetailResponse> updateShopProfile(
            @RequestHeader("authorization_shop") String token,
            @RequestBody ShopProfileDetailRequest shopProfileDetailRequest) {
        return CommonResponseEntity.success(
                shopProfileFacade.updateShopProfile(token, shopProfileDetailRequest));
    }

    @PutMapping(value = "/profile/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CommonResponseEntity<ShopProfileDetailResponse> updateShopProfileImage(
            @RequestHeader("authorization_shop") String token,
            @RequestPart(value = "image", required = false) MultipartFile img) {
        return CommonResponseEntity.success(
                shopProfileFacade.updateShopProfileImage(token, img));
    }

    @PostMapping(value = "/profile/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CommonResponseEntity<String> uploadShopInsideImages(
            @RequestHeader("authorization_shop") String token,
            @RequestPart(value = "image", required = false) List<MultipartFile> images) {
        shopProfileFacade.insertNewShopImage(token, images);
        return CommonResponseEntity.success("이미지 업로드가 완료됐습니다.");
    }

    @GetMapping("/groomers")
    public CommonResponseEntity<List<GroomerProfileDetailResponse>> getShopGroomerList(
            @RequestHeader("authorization_shop") String token) {
        return CommonResponseEntity.success(shopProfileFacade.getShopGroomerList(token));
    }

    @GetMapping("/profile/images")
    public CommonResponseEntity<List<String>> getShopImageList(
            @RequestHeader("authorization_shop") String token) {
        return CommonResponseEntity.success(shopProfileFacade.getShopImageList(token));
    }

    @GetMapping("/groomers/{shopId}")
    public CommonResponseEntity<List<GroomerProfileDetailResponse>> getShopGroomerList(
            @PathVariable Long shopId) {
        return CommonResponseEntity.success(shopProfileFacade.getShopGroomerList(shopId));
    }

    @GetMapping("/profile/images/{shopId}")
    public CommonResponseEntity<List<String>> getShopImageList(
            @PathVariable Long shopId) {
        return CommonResponseEntity.success(shopProfileFacade.getShopImageList(shopId));
    }
}
