package kr.com.duri.groomer.application.service;

import java.util.Optional;

import kr.com.duri.groomer.domain.entity.Shop;

public interface ShopService {
    boolean existsByShopId(Long shopId);

    Optional<Shop> findBySocialId(String socialId);

    Shop saveNewShop(String email, String provider);

    Shop findById(Long shopId);
}
