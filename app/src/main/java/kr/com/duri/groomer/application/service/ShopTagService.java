package kr.com.duri.groomer.application.service;

import java.util.List;

import kr.com.duri.groomer.domain.entity.Shop;

public interface ShopTagService {
    List<String> findTagsByShopId(Long shopId);

    List<String> updateShopTags(Shop shop, List<String> tags);

    void removeAllTags(Shop shop);
}
