package kr.com.duri.groomer.controller;

import kr.com.duri.common.response.CommonResponseEntity;
import kr.com.duri.groomer.application.dto.request.ShopOnboardingRequest;
import kr.com.duri.groomer.application.dto.response.ShopOnboardingResponse;
import kr.com.duri.groomer.application.facade.ShopOnboardingFacade;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/shop")
public class ShopOnboardingController {

    private final ShopOnboardingFacade shopOnboardingFacade;

    @PostMapping("/onboarding")
    public CommonResponseEntity<ShopOnboardingResponse> shopAndGroomerOnboarding(
            @RequestHeader("authorization_shop") String token,
            @RequestBody ShopOnboardingRequest shopOnboardingRequest) {
        try {
            return CommonResponseEntity.success(
                    shopOnboardingFacade.shopAndGroomerOnboarding(token, shopOnboardingRequest));
        } catch (Exception e) {
            return CommonResponseEntity.error(HttpStatus.BAD_REQUEST, "매장, 미용사 등록에 실패했습니다.");
        }
    }
}
