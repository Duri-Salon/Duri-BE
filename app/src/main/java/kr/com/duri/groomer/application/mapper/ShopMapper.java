package kr.com.duri.groomer.application.mapper;

import java.time.LocalTime;
import java.util.List;

import kr.com.duri.common.Mapper.CommonMapper;
import kr.com.duri.groomer.application.dto.response.*;
import kr.com.duri.groomer.application.dto.response.ShopDetailResponse;
import kr.com.duri.groomer.application.dto.response.ShopNearByResponse;
import kr.com.duri.groomer.application.dto.response.ShopProfileDetailResponse;
import kr.com.duri.groomer.domain.entity.Groomer;
import kr.com.duri.groomer.domain.entity.Shop;
import kr.com.duri.groomer.domain.entity.ShopImage;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ShopMapper {

    private final CommonMapper commonMapper;

    // Shop Entity to ShopDetailResponse DTO
    public ShopDetailResponse toShopDetailResponse(Shop shop, ShopImage shopImage) {
        return ShopDetailResponse.builder()
                .id(shop.getId())
                .name(shop.getName())
                .address(shop.getAddress())
                .imageURL(shopImage != null ? shopImage.getShopImageUrl() : "")
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

    // Object[] result -> ShopNearByResponse로 변환
    public ShopNearByResponse toShopNearByResponse(
            Object[] result, List<String> tags, Integer reviewCnt, String imageURL) {
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

        return ShopNearByResponse.builder()
                .shopId(shopId) // 매장 ID
                .shopImage(imageURL)
                .shopName(shopName) // 매장 이름
                .shopAddress(shopAddress) // 매장 주소
                .shopLat(shopLat) // 매장 위도
                .shopLon(shopLon) // 매장 경도
                .shopPhone(shopPhone) // 매장 전화번호
                .shopOpenTime(shopOpenTime) // 매장 오픈시간
                .shopCloseTime(shopCloseTime) // 매장 마감시간
                .shopRating(shopRating) // 매장 평균 평점
                .reviewCnt(reviewCnt)
                .distance(distance) // 중심으로부터 거리
                .tags(tags) // 태그들
                .build();
    }

    public GetShopDetailResponse toGetShopDetailResponse(
            Shop shop,
            Groomer groomer,
            ShopImage shopImage,
            List<String> shopImageUrls,
            List<String> tags,
            Integer reviewCnt,
            Integer distance) {

        ShopNearByResponse shopDetail =
                ShopNearByResponse.builder()
                        .shopId(shop.getId())
                        .shopImage(shopImage.getShopImageUrl())
                        .shopName(shop.getName())
                        .shopAddress(shop.getAddress())
                        .shopLat(shop.getLat())
                        .shopLon(shop.getLon())
                        .shopPhone(shop.getPhone())
                        .shopOpenTime(shop.getOpenTime())
                        .shopCloseTime(shop.getCloseTime())
                        .shopRating(shop.getRating())
                        .reviewCnt(reviewCnt)
                        .distance(distance)
                        .tags(tags)
                        .build();

        List<String> licenseToString = commonMapper.toListString(groomer.getLicense());
        GroomerProfileDetailResponse groomerProfileDetail =
                GroomerProfileDetailResponse.builder()
                        .id(groomer.getId())
                        .email(groomer.getEmail())
                        .phone(groomer.getPhone())
                        .name(groomer.getName())
                        .gender(groomer.getGender().toString())
                        .age(groomer.getAge())
                        .history(groomer.getHistory())
                        .image(groomer.getImage())
                        .info(groomer.getInfo())
                        .license(licenseToString)
                        .build();

        return GetShopDetailResponse.builder()
                .shopDetail(shopDetail)
                .groomerProfileDetail(groomerProfileDetail)
                .shopImages(shopImageUrls)
                .build();
    }

    public ShopProfileDetailResponse toShopProfileDetailResponse(
            Shop shop, String imageUrl, List<String> tags) {
        return ShopProfileDetailResponse.builder()
                .id(shop.getId())
                .name(shop.getName())
                .address(shop.getAddress())
                .imageURL(imageUrl)
                .phone(shop.getPhone())
                .openTime(shop.getOpenTime().toString())
                .closeTime(shop.getCloseTime().toString())
                .info(shop.getInfo())
                .kakaoTalk(shop.getKakaoTalk())
                .rating(shop.getRating())
                .tags(tags)
                .build();
    }
}
