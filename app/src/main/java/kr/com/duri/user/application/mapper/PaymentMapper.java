package kr.com.duri.user.application.mapper;

import kr.com.duri.groomer.domain.entity.Quotation;
import kr.com.duri.user.application.dto.request.ConfirmPaymentRequest;
import kr.com.duri.user.domain.Enum.PaymentStatus;
import kr.com.duri.user.domain.entity.Payment;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapper {

    // Payment 객체 생성
    public Payment toPayment(ConfirmPaymentRequest confirmPaymentRequest, Quotation quotation) {
        return Payment.builder()
                .tossOrderId(confirmPaymentRequest.getOrderId())
                .quotation(quotation)
                .tossKey(confirmPaymentRequest.getPaymentKey())
                .originalPrice(confirmPaymentRequest.getAmount())
                .price(confirmPaymentRequest.getAmount())
                .status(PaymentStatus.SUCCESS)
                .build();
    }
}