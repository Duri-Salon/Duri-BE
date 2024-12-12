package kr.com.duri.groomer.application.facade;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import kr.com.duri.groomer.application.dto.response.MonthIncomeResponse;
import kr.com.duri.groomer.application.dto.response.ShopDetailResponse;
import kr.com.duri.groomer.application.dto.response.ShopNearByResponse;
import kr.com.duri.groomer.application.mapper.ShopMapper;
import kr.com.duri.groomer.application.service.ShopImageService;
import kr.com.duri.groomer.application.service.ShopService;
import kr.com.duri.groomer.application.service.ShopTagService;
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
    private final ShopMapper shopMapper;

    // 매장 조회
    private Shop getShop(Long shopId) {
        return shopService.findById(shopId);
    }

    // 매장 상세정보 조회
    public ShopDetailResponse getShopDetail(String token) {
        Long shopId = shopService.getShopIdByToken(token);
        Shop shop = getShop(shopId);
        // TODO : 매장 이미지 조회 구현 및 연결
        ShopImage shopImage = new ShopImage(); // shopImageService.?(shopId)
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
                            String imageURL =
                                    shopImageService.getMainShopImage(shop).getShopImageUrl();
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

    // 총 매출 통계 조회
    public MonthIncomeResponse getMonthIncome(String token) {
        Long shopId = shopService.getShopIdByToken(token);
        getShop(shopId);
        Long totalIncome = paymentService.getTotalPriceMonth(shopId);
        return shopMapper.toShopDetailResponse(totalIncome);
    }

    // 주변 매장 검색
    public List<ShopNearByResponse> searchShops(String search, Double lat, Double lon) {
        List<Object[]> shopResults = shopService.findShopsWithSearch(search, lat, lon);
        return mapToShopNearByResponses(shopResults);
    }
}
