package kr.com.duri.user.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuotationReqDetailRequest {
    private Long quotationReqId; // 견적요청서 ID
    private Double lat; // 중심 위도
    private Double lon; // 중심 경도
}
