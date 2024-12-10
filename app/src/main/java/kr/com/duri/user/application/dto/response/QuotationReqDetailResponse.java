package kr.com.duri.user.application.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuotationReqDetailResponse {
    private LocalDateTime createdAt; // 요청 생성 시간
    private LocalDateTime expiredAt; // 만료 시간
    private ShopBestResponse bestDistanceShop; // 거리순 1등 매장
    private ShopBestResponse bestPriceShop; // 가격순 1등 매장
    private ShopBestResponse bestRatingShop; // 평점순 1등 매장
    private ShopBestResponse bestShop; // 통합 1등 매장
    private List<QuotationReqDetailRequestListResponse> quotations; // 요청별 최종 견적 금액
}
