package kr.com.duri.groomer.application.service;

import java.util.List;

public interface ShopTagService {
    List<String> findTagsByShopId(Long shopId);
}
