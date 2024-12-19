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

    // 거리순 매장 목록 조회 (매장 반환)
    List<Shop> findShopswithSortAsc(Double lat, Double lon);

    // 입점 대기 목록
    List<Shop> getEntryWaitingShops();

    // 입점 승인 목록
    List<Shop> getEntryApprovedShops();

    // 입점 승인 처리
    void approveEntry(Long shopId);

    // 입점 거절 처리
    void rejectEntry(Long shopId);
}
