package kr.com.duri.groomer.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PriceDetailRequest {
    private Integer groomingPrice; // 미용 금액
    private Integer additionalPrice; // 추가 미용 금액
    private Integer specialCarePrice; // 스페셜 케어 금액
    private Integer designPrice; // 디자인 컷 금액
    private Integer customPrice; // 요구 사항 금액
    private Integer totalPrice; // 최종 금액
}
