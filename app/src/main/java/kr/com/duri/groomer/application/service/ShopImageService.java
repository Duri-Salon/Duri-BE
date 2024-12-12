package kr.com.duri.groomer.application.service;

import java.util.List;

import kr.com.duri.groomer.domain.Enum.ImageCategory;
import kr.com.duri.groomer.domain.entity.Shop;
import kr.com.duri.groomer.domain.entity.ShopImage;
import org.springframework.web.multipart.MultipartFile;

public interface ShopImageService {
    ShopImage getMainShopImage(Shop shop);

    List<ShopImage> getShopImagesList(Shop shop);

    List<ShopImage> getShopImagesListWithCategory(Shop shop, ImageCategory category);

    List<ShopImage> getShopImagesListWithCategoryNot(Shop shop, ImageCategory category);

    String uploadShopMainImage(Shop shop, MultipartFile img);

    void uploadShopImages(Shop shop, List<MultipartFile> images);
}
