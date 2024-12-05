package kr.com.duri.user.application.mapper;

import kr.com.duri.groomer.domain.entity.Quotation;
import kr.com.duri.user.application.dto.request.ConfirmPaymentRequest;
import kr.com.duri.user.application.dto.response.PaymentResponse;
import kr.com.duri.user.domain.Enum.PaymentStatus;
import kr.com.duri.user.domain.entity.Payment;
import org.json.simple.JSONObject;
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

    public PaymentResponse toPaymentResponse(JSONObject response) {
        JSONObject receipt = (JSONObject) response.get("receipt");
        String receiptUrl = receipt != null ? (String) receipt.get("url") : null;

        return new PaymentResponse(
                (String) response.get("orderId"),
                (String) response.get("paymentKey"),
                ((Number) response.get("totalAmount")).intValue(),
                (String) response.get("approvedAt"),
                receiptUrl,
                (String) response.get("status")
        );
    }

}