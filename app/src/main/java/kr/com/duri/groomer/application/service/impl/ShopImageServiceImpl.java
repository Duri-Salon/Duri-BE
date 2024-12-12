package kr.com.duri.groomer.application.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import kr.com.duri.common.s3.S3Util;
import kr.com.duri.groomer.application.service.ShopImageService;
import kr.com.duri.groomer.domain.Enum.ImageCategory;
import kr.com.duri.groomer.domain.entity.Shop;
import kr.com.duri.groomer.domain.entity.ShopImage;
import kr.com.duri.groomer.repository.ShopImageRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ShopImageServiceImpl implements ShopImageService {

    private final ShopImageRepository shopImageRepository;

    private final S3Util s3Util;

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

    @Override
    public String uploadShopMainImage(Shop shop, MultipartFile img) {
        if (img == null) {
            return null;
        }
        String imageUrl = s3Util.uploadToS3(img);
        shopImageRepository.save(ShopImage.createNewShopImage(shop, imageUrl, "MAIN"));
        return imageUrl;
    }

    @Override
    public void uploadShopImages(Shop shop, List<MultipartFile> images) {
        for (MultipartFile img : images) {
            String imageUrl = s3Util.uploadToS3(img);
            shopImageRepository.save(ShopImage.createNewShopImage(shop, imageUrl, "ETC"));
        }
    }

    @Override
    public List<String> findImagesByShop(Shop shop) {
        List<ShopImage> shopImages =
                shopImageRepository.findShopImagesByShopAndCategoryNotOrderByCreatedAtDesc(
                        shop, ImageCategory.MAIN);
        return shopImages == null
                ? Collections.emptyList()
                : shopImages.stream()
                        .map(ShopImage::getShopImageUrl)
                        .filter(Objects::nonNull)
                        .toList();
    }

    @Override
    public boolean existMainImage(Shop shop) {
        return shopImageRepository.existsByShopAndCategory(shop, ImageCategory.MAIN);
    }

    @Override
    public void deleteShopMainImage(Shop shop) {
        ShopImage shopImage =
                shopImageRepository.findShopImageByShopAndCategory(shop, ImageCategory.MAIN);
        if (shopImage != null) {
            s3Util.deleteFromS3(shopImage.getShopImageUrl());
            shopImageRepository.delete(shopImage);
        }
    }
}
