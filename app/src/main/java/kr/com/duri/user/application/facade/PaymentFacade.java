package kr.com.duri.user.application.facade;

import jakarta.servlet.http.HttpSession;
import kr.com.duri.groomer.application.service.QuotationService;
import kr.com.duri.groomer.domain.entity.Quotation;
import kr.com.duri.user.application.dto.request.ConfirmPaymentRequest;
import kr.com.duri.user.application.dto.request.SaveAmountRequest;
import kr.com.duri.user.application.dto.response.PaymentResponse;
import kr.com.duri.user.application.mapper.PaymentMapper;
import kr.com.duri.user.application.service.PaymentService;
import kr.com.duri.user.domain.entity.Payment;
import lombok.RequiredArgsConstructor;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentFacade {

    private final PaymentService paymentService;
    private final QuotationService quotationService;
    private final PaymentMapper paymentMapper;

    @Value("${toss.widget.secret.key}")
    private String widgetSecretKey;

    @Value("${toss.api.url}")
    private String tossApiUrl;

    // 결제 금액 임시 저장
    public void saveAmount(HttpSession session, SaveAmountRequest saveAmountRequest) {
        paymentService.saveAmount(session, saveAmountRequest);
    }

    // 결제 금액 검증
    public boolean verifyAmount(HttpSession session, SaveAmountRequest saveAmountRequest) {
        return paymentService.verifyAmount(session, saveAmountRequest);
    }

    // 결제 승인 요청
    public PaymentResponse confirmPayment(ConfirmPaymentRequest confirmPaymentRequest) {
        // Toss API 호출
        JSONObject response =
                paymentService.confirmPayment(confirmPaymentRequest, tossApiUrl, widgetSecretKey);
        Object status = response.get("status");

        // Quotation 조회
        Quotation quotation = quotationService.findById(confirmPaymentRequest.getQuotationId());

        // 결제 상태가 DONE일 경우 결제 성공 처리
        if ("DONE".equals(status)) {
            Payment payment = paymentMapper.toPayment(confirmPaymentRequest, quotation);
            paymentService.save(payment);
        }
        return paymentMapper.toPaymentResponse(response);
    }
}
