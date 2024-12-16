package kr.com.duri.user.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SaveAmountRequest {
    private String orderId; // 주문번호
    private Integer amount; // 결제 금액
}
