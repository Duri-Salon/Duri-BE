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
                            Long shopId = (Long) result[0]; // shopId
                            String shopName = (String) result[1]; // shopName
                            String shopAddress = (String) result[2]; // shopAddress
                            Double shopLat = (Double) result[3]; // shopLat
                            Double shopLon = (Double) result[4]; // shopLon
                            String shopPhone = (String) result[5]; // shopPhone
                            LocalTime shopOpenTime =
                                    ((java.sql.Time) result[6]).toLocalTime(); // shopOpenTime
                            LocalTime shopCloseTime =
                                    ((java.sql.Time) result[7]).toLocalTime(); // shopCloseTime
                            Float shopRating = (Float) result[8]; // shopRating
                            Integer distance = (int) Math.round((Double) result[9]); // distance

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
