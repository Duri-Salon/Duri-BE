package kr.com.duri.groomer.controller;

import kr.com.duri.common.response.CommonResponseEntity;
import kr.com.duri.groomer.application.dto.response.NewShopJwtResponse;
import kr.com.duri.groomer.application.facade.ShopAuthFacade;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class ShopAuthController {

    private final ShopAuthFacade shopAuthFacade;

    @GetMapping("/shop/token")
    public CommonResponseEntity<NewShopJwtResponse> requestNewShopToken(
            @RequestParam String providerId) {
        return CommonResponseEntity.success(shopAuthFacade.createNewShopJwt(providerId));
    }
}
