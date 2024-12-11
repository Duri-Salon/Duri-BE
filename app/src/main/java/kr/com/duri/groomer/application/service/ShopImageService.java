package kr.com.duri.groomer.application.service;

import java.util.List;

import kr.com.duri.groomer.domain.Enum.ImageCategory;
import kr.com.duri.groomer.domain.entity.Shop;
import kr.com.duri.groomer.domain.entity.ShopImage;

public interface ShopImageService {
    ShopImage getMainShopImage(Shop shop);

    List<ShopImage> getShopImagesList(Shop shop);

    List<ShopImage> getShopImagesListWithCategory(Shop shop, ImageCategory category);

    List<ShopImage> getShopImagesListWithCategoryNot(Shop shop, ImageCategory category);
}
