package kr.com.duri.user.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuotationReqDetailRequestListResponse {
    private Long requestId; // 요청 ID
    private String shopName; // 매장 이름
    private Integer totalPrice; // 최종 견적 금액
}
