package kr.com.duri.groomer.application.service;

import kr.com.duri.groomer.domain.entity.Shop;

import java.util.List;

public interface ShopTagService {
    List<String> findTagsByShopId(Long shopId);

    List<String> updateShopTags(Shop shop, List<String> tags);

    void removeAllTags(Shop shop);
}
