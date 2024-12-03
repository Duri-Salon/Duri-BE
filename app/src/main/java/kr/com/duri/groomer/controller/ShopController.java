package kr.com.duri.groomer.controller;

import java.util.List;

import kr.com.duri.common.response.CommonResponseEntity;
import kr.com.duri.groomer.application.dto.response.MonthIncomeResponse;
import kr.com.duri.groomer.application.dto.response.ShopDetailResponse;
import kr.com.duri.groomer.application.dto.response.ShopNearByResponse;
import kr.com.duri.groomer.application.facade.GroomerHomeFacade;
import kr.com.duri.groomer.application.facade.ShopFacade;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/shop")
public class ShopController {

    private final ShopFacade shopFacade;
    private final GroomerHomeFacade homeFacade;

    // DURI-260 : 매장 상세정보 조회
    @GetMapping("/{shopId}")
    public CommonResponseEntity<ShopDetailResponse> getShopDetail(@PathVariable Long shopId) {
        return CommonResponseEntity.success(shopFacade.getShopDetail(shopId));
    }

    // 주변 매장 리스트 조회
    @GetMapping("/nearby")
    public CommonResponseEntity<List<ShopNearByResponse>> getNearByShops(
            @RequestParam double lat, @RequestParam double lon, @RequestParam double radius) {
        List<ShopNearByResponse> response = shopFacade.getNearByShops(lat, lon, radius);
        return CommonResponseEntity.success(response);
    }

    // 주변 매장 리스트 조회(거리순)
    @GetMapping("/nearby/sort/distance")
    public CommonResponseEntity<List<ShopNearByResponse>> getNearByShopsDistance(
            @RequestParam double lat, @RequestParam double lon, @RequestParam double radius) {
        List<ShopNearByResponse> response = shopFacade.getNearByShopsDistance(lat, lon, radius);
        return CommonResponseEntity.success(response);
    }

    // 주변 매장 리스트 조회(평점순)
    @GetMapping("/nearby/sort/rating")
    public CommonResponseEntity<List<ShopNearByResponse>> getNearByShopsRating(
            @RequestParam double lat, @RequestParam double lon, @RequestParam double radius) {
        List<ShopNearByResponse> response = shopFacade.getNearByShopsRating(lat, lon, radius);
        return CommonResponseEntity.success(response);
    }

    // DURI-322 : 이번달 총 매출 조회
    @GetMapping("/Income/{shopId}")
    public CommonResponseEntity<MonthIncomeResponse> getMonthIncome(@PathVariable Long shopId) {
        return CommonResponseEntity.success(shopFacade.getMonthIncome(shopId));
    }
}
