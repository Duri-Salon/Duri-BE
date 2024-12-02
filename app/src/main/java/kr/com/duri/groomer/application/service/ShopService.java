package kr.com.duri.groomer.application.service;

import kr.com.duri.groomer.domain.entity.Shop;

import java.util.Optional;

public interface ShopService {
    boolean existsByShopId(Long shopId);

    Optional<Shop> findBySocialId(String socialId);

    Shop saveNewShop(String email, String provider);
}
