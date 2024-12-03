package kr.com.duri.groomer.application.facade;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import kr.com.duri.groomer.application.dto.response.ShopNearByResponse;
import kr.com.duri.groomer.application.mapper.ShopMapper;
import kr.com.duri.groomer.application.service.ShopService;
import kr.com.duri.groomer.application.service.ShopTagService;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShopFacade {
    private final ShopService shopService;
    private final ShopTagService shopTagService;
    private final ShopMapper shopMapper;

    private List<ShopNearByResponse> mapToShopNearByResponses(List<Object[]> shopResults) {
        return shopResults.stream()
                .map(
                        result -> {
                            Long shopId = (Long) result[0];
                            List<String> tags = shopTagService.findTagsByShopId(shopId);
                            return shopMapper.toShopNearByResponse(result, tags);
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
}
