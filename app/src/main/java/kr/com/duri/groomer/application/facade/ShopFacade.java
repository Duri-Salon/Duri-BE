package kr.com.duri.groomer.application.facade;

import java.time.LocalTime;
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

    public List<ShopNearByResponse> getNearbyShops(Double lat, Double lon, Double radius) {
        // 1. 반경 내 매장 조회
        List<Object[]> shopResults = shopService.findShopsWithinRadius(lat, lon, radius);

        for (Object[] shopResult : shopResults) {
            System.out.println(shopResult[1]);
        }
        // 2. 매장 정보 DTO로 변환
        return shopResults.stream()
                .map(
                        result -> {
                            Long shopId = (Long) result[0];
                            String shopName = (String) result[1];
                            String shopAddress = (String) result[2];
                            Double shopLat = (Double) result[3];
                            Double shopLon = (Double) result[4];
                            String shopPhone = (String) result[5];
                            LocalTime shopOpenTime =
                                    ((java.sql.Time) result[6]).toLocalTime();
                            LocalTime shopCloseTime =
                                    ((java.sql.Time) result[7]).toLocalTime();
                            Float shopRating = (Float) result[8];
                            Integer distance = (int) Math.round((Double) result[9]);

                            List<String> tags = shopTagService.findTagsByShopId(shopId);

                            return shopMapper.toShopNearByResponse(
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
                        })
                .collect(Collectors.toList());
    }
}
