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
public class QuotationListResponse {
    private Long quotationReqId; // 견적 요청 ID
    private Long requestId; // 요청 1개 ID
    private LocalDateTime createdAt; // 생성 시각
    private LocalDateTime expiredAt; // 만료 시각
    private List<QuotationListShopResponse> shops; // 관련 매장 목록
    private Boolean isExpired; // 만료 여부
}
