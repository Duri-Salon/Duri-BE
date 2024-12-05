package kr.com.duri.user.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConfirmPaymentRequest {
    private String paymentKey; // 토스 결제 키
    private String orderId; // 주문 ID
    private Integer amount; // 결제 금액
    private Long quotationId; // 견적서 ID
}
