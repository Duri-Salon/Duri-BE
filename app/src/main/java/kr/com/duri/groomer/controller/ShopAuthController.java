package kr.com.duri.groomer.controller;

import kr.com.duri.common.response.CommonResponseEntity;
import kr.com.duri.groomer.application.dto.response.NewShopJwtResponse;
import kr.com.duri.groomer.application.facade.ShopAuthFacade;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class ShopAuthController {

    private final ShopAuthFacade shopAuthFacade;

    @GetMapping("/shop/token")
    public CommonResponseEntity<NewShopJwtResponse> requestNewShopToken(@RequestParam String providerId) {
        NewShopJwtResponse newShopJwtResponse = null;

        try {
            newShopJwtResponse = shopAuthFacade.createNewShopJwt(providerId);
        } catch (IllegalArgumentException e) {
            return CommonResponseEntity.error(HttpStatus.BAD_REQUEST, "토큰 생성에 실패했습니다.");
        }

        return CommonResponseEntity.success(newShopJwtResponse);
    }

}
