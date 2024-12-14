package kr.com.duri.groomer.application.facade;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import kr.com.duri.groomer.application.dto.response.GetShopDetailResponse;
import kr.com.duri.groomer.application.dto.response.ShopDetailResponse;
import kr.com.duri.groomer.application.dto.response.ShopNearByResponse;
import kr.com.duri.groomer.application.mapper.ShopMapper;
import kr.com.duri.groomer.application.service.GroomerService;
import kr.com.duri.groomer.application.service.ShopImageService;
import kr.com.duri.groomer.application.service.ShopService;
import kr.com.duri.groomer.application.service.ShopTagService;
import kr.com.duri.groomer.domain.Enum.ImageCategory;
import kr.com.duri.groomer.domain.entity.Groomer;
import kr.com.duri.groomer.domain.entity.Shop;
import kr.com.duri.groomer.domain.entity.ShopImage;
import kr.com.duri.user.application.service.PaymentService;
import kr.com.duri.user.application.service.ReviewService;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShopFacade {
    private final ShopService shopService;
    private final ReviewService reviewService;
    private final ShopTagService shopTagService;
    private final PaymentService paymentService;
    private final ShopImageService shopImageService;
    private final GroomerService groomerService;
    private final ShopMapper shopMapper;

    // 매장 조회
    private Shop getShop(Long shopId) {
        return shopService.findById(shopId);
    }

    // 매장 상세정보 조회
    public ShopDetailResponse getShopDetail(String token) {
        Long shopId = shopService.getShopIdByToken(token);
        Shop shop = getShop(shopId);
        ShopImage shopImage = shopImageService.getMainShopImage(shop);
        return shopMapper.toShopDetailResponse(shop, shopImage);
    }

    private List<ShopNearByResponse> mapToShopNearByResponses(List<Object[]> shopResults) {
        return shopResults.stream()
                .map(
                        result -> {
                            Long shopId = (Long) result[0];
                            List<String> tags = shopTagService.findTagsByShopId(shopId);
                            Integer reviewCnt = reviewService.getReviewsByShopId(shopId).size();

                            Shop shop = shopService.findById(shopId);
                            ShopImage mainImage = shopImageService.getMainShopImage(shop);
                            String imageURL =
                                    (mainImage != null) ? mainImage.getShopImageUrl() : null;
                            return shopMapper.toShopNearByResponse(
                                    result, tags, reviewCnt, imageURL);
                        })
                .collect(Collectors.toList());
    }

    // 주변 매장 리스트 조회
    public List<ShopNearByResponse> getNearByShops(Double lat, Double lon, Double radius) {
        List<Object[]> shopResults = shopService.findShopsWithinRadius(lat, lon, radius);
        return mapToShopNearByResponses(shopResults);
    }

    // 주변 매장 리스트 조회 (거리순)
    public List<ShopNearByResponse> getNearByShopsDistance(Double lat, Double lon, Double radius) {
        List<Object[]> shopResults = shopService.findShopsWithinRadius(lat, lon, radius);
        return mapToShopNearByResponses(shopResults).stream()
                .sorted(Comparator.comparingInt(ShopNearByResponse::getDistance))
                .collect(Collectors.toList());
    }

    // 주변 매장 리스트 조회 (평점순)
    public List<ShopNearByResponse> getNearByShopsRating(Double lat, Double lon, Double radius) {
        List<Object[]> shopResults = shopService.findShopsWithinRadius(lat, lon, radius);
        return mapToShopNearByResponses(shopResults).stream()
                .sorted(Comparator.comparingDouble(ShopNearByResponse::getShopRating).reversed())
                .collect(Collectors.toList());
    }

    // 주변 매장 검색
    public List<ShopNearByResponse> searchShops(String search, Double lat, Double lon) {
        List<Object[]> shopResults = shopService.findShopsWithSearch(search, lat, lon);
        return mapToShopNearByResponses(shopResults);
    }

    // 매장 상세정보 조회(유저용)
    public GetShopDetailResponse getShopDetailByShopId(Long shopId, double lat, double lon) {
        Shop shop = getShop(shopId); // 매장
        Groomer groomer = groomerService.getGroomerByShopId(shopId); // 미용사

        // 매장 이미지
        ShopImage shopMainImage =
                Optional.ofNullable(shopImageService.getMainShopImage(shop))
                        .orElse(new ShopImage()); // 메인이미지
        List<ShopImage> shopImages =
                Optional.ofNullable(
                                shopImageService.getShopImagesListWithCategoryNot(
                                        shop, ImageCategory.MAIN))
                        .orElse(Collections.emptyList()); // Main 카테고리 빼고 다
        List<String> shopImageUrls =
                shopImages.stream()
                        .map(
                                shopImage ->
                                        Optional.ofNullable(shopImage.getShopImageUrl())
                                                .orElse("No Image"))
                        .collect(Collectors.toList());

        // 태그 처리 / 리뷰개수 / 거리
        List<String> tags =
                Optional.ofNullable(shopTagService.findTagsByShopId(shopId))
                        .orElse(Collections.emptyList());
        Integer reviewCnt = reviewService.getReviewsByShopId(shopId).size();
        Integer distance =
                (int) Math.floor(calculateDistance(shop.getLat(), shop.getLon(), lat, lon));

        return shopMapper.toGetShopDetailResponse(
                shop, groomer, shopMainImage, shopImageUrls, tags, reviewCnt, distance);
    }

    // 거리 계산
    public Double calculateDistance(
            double shopLat, double shopLon, double requestLat, double requestLon) {
        double lat1 = requestLat;
        double lon1 = requestLon;

        double lat2 = shopLat;
        double lon2 = shopLon;

        // 지구 반지름 (미터 단위)
        double earthRadius = 6371000;

        // 위도, 경도를 라디안으로 변환
        double lat1Rad = Math.toRadians(lat1);
        double lon1Rad = Math.toRadians(lon1);
        double lat2Rad = Math.toRadians(lat2);
        double lon2Rad = Math.toRadians(lon2);

        // 거리 계산
        double dLon = lon2Rad - lon1Rad;
        double a =
                Math.cos(lat1Rad) * Math.cos(lat2Rad) * Math.cos(dLon)
                        + Math.sin(lat1Rad) * Math.sin(lat2Rad);

        // ACOS 사용
        double c = Math.acos(a);

        // 거리 반환 (미터 단위로 반환, Double 타입)
        return earthRadius * c; // 미터 단위, 소수점 가능
    }
}
