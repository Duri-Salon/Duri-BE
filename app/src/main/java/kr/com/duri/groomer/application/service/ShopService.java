package kr.com.duri.groomer.application.service;

import java.util.List;
import java.util.Optional;

import kr.com.duri.groomer.application.dto.request.ShopDetailRequest;
import kr.com.duri.groomer.domain.entity.Shop;

public interface ShopService {
    boolean existsByShopId(Long shopId);

    Optional<Shop> findBySocialId(String socialId);

    Shop saveNewShop(String email, String provider);

    Shop findById(Long shopId);

    String createNewShopJwt(Shop shop);

    List<Object[]> findShopsWithinRadius(Double lat, Double lon, Double radius);

    List<Object[]> findShopsWithSearch(String search, Double lat, Double lon);

    Long getShopIdByToken(String token);

    Shop updateDetail(Shop shop, ShopDetailRequest shopDetailRequest);
}
