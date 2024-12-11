package kr.com.duri.groomer.application.service.impl;

import java.util.List;

import kr.com.duri.groomer.application.service.ShopImageService;
import kr.com.duri.groomer.domain.Enum.ImageCategory;
import kr.com.duri.groomer.domain.entity.Shop;
import kr.com.duri.groomer.domain.entity.ShopImage;
import kr.com.duri.groomer.repository.ShopImageRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShopImageServiceImpl implements ShopImageService {

    private final ShopImageRepository shopImageRepository;

    @Override
    public ShopImage getMainShopImage(Shop shop) {
        return shopImageRepository.findShopImageByShopAndCategory(shop, ImageCategory.MAIN);
    }

    @Override
    public List<ShopImage> getShopImagesList(Shop shop) {
        return shopImageRepository.findShopImagesByShopAndCategoryNotOrderByCreatedAtDesc(
                shop, ImageCategory.MAIN);
    }

    @Override
    public List<ShopImage> getShopImagesListWithCategory(Shop shop, ImageCategory category) {
        return shopImageRepository.findShopImagesByShopAndCategoryOrderByCreatedAtDesc(
                shop, category);
    }

    @Override
    public List<ShopImage> getShopImagesListWithCategoryNot(Shop shop, ImageCategory category) {
        return shopImageRepository.findShopImagesByShopAndCategoryNotOrderByCreatedAtDesc(
                shop, category);
    }
}
