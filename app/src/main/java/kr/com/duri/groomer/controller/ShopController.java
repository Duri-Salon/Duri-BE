package kr.com.duri.groomer.controller;

import java.util.List;

import kr.com.duri.common.response.CommonResponseEntity;
import kr.com.duri.groomer.application.dto.response.GetShopDetailResponse;
import kr.com.duri.groomer.application.dto.response.ShopNearByResponse;
import kr.com.duri.groomer.application.dto.response.ShopProfileDetailResponse;
import kr.com.duri.groomer.application.facade.ShopFacade;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/shop")
public class ShopController {

    private final ShopFacade shopFacade;

    // DURI-260 : 매장 상세정보 조회
    @GetMapping
    public CommonResponseEntity<ShopProfileDetailResponse> getShopDetail(
            @RequestHeader("authorization_shop") String token) {
        return CommonResponseEntity.success(shopFacade.getShopDetail(token));
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

    // 주변 매장 검색
    @GetMapping("/search")
    public CommonResponseEntity<List<ShopNearByResponse>> searchShops(
            @RequestParam String search, @RequestParam double lat, @RequestParam double lon) {

        List<ShopNearByResponse> response = shopFacade.searchShops(search, lat, lon);
        return CommonResponseEntity.success(response);
    }

    // 매장 상세정보 조회(유저용)
    @GetMapping("/detail")
    public CommonResponseEntity<GetShopDetailResponse> getShopDetailByShopId(
            @RequestParam Long shopId, @RequestParam double lat, @RequestParam double lon) {
        return CommonResponseEntity.success(shopFacade.getShopDetailByShopId(shopId, lat, lon));
    }
}
