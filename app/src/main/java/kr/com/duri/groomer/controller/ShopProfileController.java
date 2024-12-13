package kr.com.duri.groomer.controller;

import java.util.List;

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

    @PostMapping("/onboarding")
    public CommonResponseEntity<ShopOnboardingResponse> shopAndGroomerOnboarding(
            @RequestHeader("authorization_shop") String token,
            @RequestBody ShopOnboardingRequest shopOnboardingRequest) {
        return CommonResponseEntity.success(
                shopProfileFacade.shopAndGroomerOnboarding(token, shopOnboardingRequest));
    }

    @PutMapping(value = "/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CommonResponseEntity<ShopProfileDetailResponse> updateShopProfile(
            @RequestHeader("authorization_shop") String token,
            @RequestPart("shopProfileDetailRequest")
                    ShopProfileDetailRequest shopProfileDetailRequest,
            @RequestPart(value = "image", required = false) MultipartFile img) {
        return CommonResponseEntity.success(
                shopProfileFacade.updateShopProfile(token, shopProfileDetailRequest, img));
    }

    @PostMapping(value = "/profile/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CommonResponseEntity<String> uploadShopProfile(
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
}