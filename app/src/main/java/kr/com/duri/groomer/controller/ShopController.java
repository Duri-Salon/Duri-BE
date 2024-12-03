package kr.com.duri.groomer.controller;

import java.util.List;

import kr.com.duri.common.response.CommonResponseEntity;
import kr.com.duri.groomer.application.dto.response.ShopNearByResponse;
import kr.com.duri.groomer.application.facade.ShopFacade;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/shop")
public class ShopController {

    private final ShopFacade shopFacade;

    @GetMapping("/nearby")
    public CommonResponseEntity<List<ShopNearByResponse>> getNearByShops(
            @RequestParam double lat, @RequestParam double lon, @RequestParam double radius) {
        List<ShopNearByResponse> response = shopFacade.getNearbyShops(lat, lon, radius);
        return CommonResponseEntity.success(response);
    }
}
