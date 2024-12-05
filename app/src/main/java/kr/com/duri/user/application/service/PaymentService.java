package kr.com.duri.user.application.service;

import jakarta.servlet.http.HttpSession;
import kr.com.duri.user.application.dto.request.ConfirmPaymentRequest;
import kr.com.duri.user.application.dto.request.SaveAmountRequest;
import kr.com.duri.user.domain.entity.Payment;

import org.json.simple.JSONObject;

public interface PaymentService {
    // 결제 금액 임시 저장
    void saveAmount(HttpSession session, SaveAmountRequest saveAmountRequest);

    // 결제 금액 검증
    boolean verifyAmount(HttpSession session, SaveAmountRequest saveAmountRequest);

    // Payment정보 DB저장
    Payment save(Payment payment);

    // 결제 승인 요청 처리
    JSONObject confirmPayment(
            ConfirmPaymentRequest confirmPaymentRequest, String tossApiUrl, String widgetSecretKey);
    // 월별 총 매출액 조회
    Long getTotalPriceMonth(Long shopId);
}
