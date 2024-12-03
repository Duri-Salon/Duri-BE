package kr.com.duri.groomer.application.mapper;

import java.time.LocalTime;
import java.util.List;
import kr.com.duri.groomer.application.dto.response.ShopDetailResponse;
import kr.com.duri.groomer.application.dto.response.ShopNearByResponse;
import kr.com.duri.groomer.domain.entity.Shop;
import kr.com.duri.groomer.domain.entity.ShopImage;

import org.springframework.stereotype.Component;

@Component
public class ShopMapper {

  // Shop Entity to ShopDetailResponse DTO
  public ShopDetailResponse toShopDetailResponse(Shop shop, ShopImage shopImage) {
    return ShopDetailResponse.builder()
        .id(shop.getId())
        .name(shop.getName())
        .address(shop.getAddress())
        .imageURL(shopImage.getShopImageUrl())
        .build();
  }

  public ShopNearByResponse toShopNearByResponse(
      Long shopId,
      String shopName,
      String shopAddress,
      Double shopLat,
      Double shopLon,
      String shopPhone,
      LocalTime shopOpenTime,
      LocalTime shopCloseTime,
      Float shopRating,
      Integer distance,
      List<String> tags) {
    return ShopNearByResponse.builder()
        .shopId(shopId) // 매장 ID
        .shopName(shopName) // 매장 이름
        .shopAddress(shopAddress) // 매장 주소
        .shopLat(shopLat) // 매장 위도
        .shopLon(shopLon) // 매장 경도
        .shopPhone(shopPhone) // 매장 전화번호
        .shopOpenTime(shopOpenTime) // 매장 오픈시간
        .shopCloseTime(shopCloseTime) // 매장 마감시간
        .shopRating(shopRating) // 매장 평균 평점
        .distance(distance) // 중심으로부터 거리
        .tags(tags) // 태그들
        .build();
  }
}
