package kr.com.duri.groomer.repository;

import java.util.List;

import kr.com.duri.groomer.domain.Enum.ImageCategory;
import kr.com.duri.groomer.domain.entity.Shop;
import kr.com.duri.groomer.domain.entity.ShopImage;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ShopImageRepository extends JpaRepository<ShopImage, Long> {
    ShopImage findShopImageByShopAndCategory(Shop shop, ImageCategory category);

    // 해당 매장의 이미지들을 카테고리별로 리스트 조회
    List<ShopImage> findShopImagesByShopAndCategoryOrderByCreatedAtDesc(
            Shop shop, ImageCategory category);

    // 해당 매장의 이미지들을 선택 카테고리별 제외한 리스트 조회
    List<ShopImage> findShopImagesByShopAndCategoryNotOrderByCreatedAtDesc(
            Shop shop, ImageCategory category);

    boolean existsByShopAndCategory(Shop shop, ImageCategory imageCategory);
}
