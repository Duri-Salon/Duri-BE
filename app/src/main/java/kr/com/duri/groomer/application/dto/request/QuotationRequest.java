package kr.com.duri.groomer.application.dto.request;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuotationRequest {
    private Long requestId; // 견적 요청서 ID
    private PriceDetailRequest priceDetail; // 가격 정보
    private String memo; // 전달 사항
    private LocalDateTime startDateTime; // 미용 시작 시간
    private LocalDateTime endDateTime; // 미용 종료 시간
}
