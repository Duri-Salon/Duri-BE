package kr.com.duri.groomer.application.service;

import java.util.List;
import java.util.Optional;

import kr.com.duri.groomer.application.dto.request.ShopOnboardingInfo;
import kr.com.duri.groomer.application.dto.request.ShopProfileDetailRequest;
import kr.com.duri.groomer.domain.entity.Shop;

public interface ShopService {
    boolean existsByShopId(Long shopId);

    Optional<Shop> findBySocialId(String socialId);

    Shop saveNewShop(String email, String provider);

    Shop findById(Long shopId);

    String createNewShopJwt(Shop shop);

    List<Object[]> findShopsWithinRadius(Double lat, Double lon, Double radius);

    List<Object[]> findShopsWithSearch(String search, Double lat, Double lon);

    Shop updateDetail(Shop shop, ShopOnboardingInfo shopOnboardingInfo);

    Shop updateShopRating(Long shopId, Float rating);

    // 반경 내 매장 목록 조회
    List<Shop> findShopsByRadius(Double lat, Double lon, Double radians);

    // 토큰으로 매장 아이디 찾기
    Long getShopIdByToken(String token);

    Shop updateDetail(Shop shop, ShopProfileDetailRequest shopProfileDetailRequest);
}
