package kr.com.duri.user.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentResponse {
    private String orderId; // 토스에서 관리하는 OrderID
    private String paymentKey; // Payment Key
    private int totalAmount; // 결제 금액
    private String approvedAt; // 결제 시간
    private String receiptUrl; // 결제 내역 영수증URL
    private String status; // 결제 상태
}
