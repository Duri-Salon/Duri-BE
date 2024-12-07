package kr.com.duri.groomer.application.mapper;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import kr.com.duri.groomer.application.dto.response.MonthIncomeResponse;
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
                .phone(shop.getPhone())
                .build();
    }

    public ShopDetailResponse toShopDetailResponse(Shop shop) {
        return ShopDetailResponse.builder()
                .id(shop.getId())
                .name(shop.getName())
                .address(shop.getAddress())
                .imageURL(null)
                .phone(shop.getPhone())
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

    // Object[] result -> ShopNearByResponse로 변환
    public ShopNearByResponse toShopNearByResponse(Object[] result, List<String> tags) {
        Long shopId = (Long) result[0];
        String shopName = (String) result[1];
        String shopAddress = (String) result[2];
        Double shopLat = (Double) result[3];
        Double shopLon = (Double) result[4];
        String shopPhone = (String) result[5];
        LocalTime shopOpenTime = ((java.sql.Time) result[6]).toLocalTime();
        LocalTime shopCloseTime = ((java.sql.Time) result[7]).toLocalTime();
        Float shopRating = (Float) result[8];
        Integer distance = (int) Math.round((Double) result[9]);

        return toShopNearByResponse(
                shopId,
                shopName,
                shopAddress,
                shopLat,
                shopLon,
                shopPhone,
                shopOpenTime,
                shopCloseTime,
                shopRating,
                distance,
                tags);
    }

    // month, totalIncome to MonthIncomeResponse DTO
    public MonthIncomeResponse toShopDetailResponse(Long totalIncome) {
        DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MM");
        return MonthIncomeResponse.builder()
                .month(LocalDateTime.now().format(monthFormatter))
                .income(totalIncome != null ? totalIncome : 0L)
                .build();
    }
}
