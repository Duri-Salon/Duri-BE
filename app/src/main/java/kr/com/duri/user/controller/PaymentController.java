package kr.com.duri.user.controller;

import java.util.UUID;

import jakarta.servlet.http.HttpSession;
import kr.com.duri.common.response.CommonResponseEntity;
import kr.com.duri.user.application.dto.request.ConfirmPaymentRequest;
import kr.com.duri.user.application.dto.request.SaveAmountRequest;
import kr.com.duri.user.application.dto.response.GenerateKeyResponse;
import kr.com.duri.user.application.dto.response.PaymentResponse;
import kr.com.duri.user.application.facade.PaymentFacade;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payments")
public class PaymentController {

    private final PaymentFacade paymentFacade;

    // 결제 금액 임시 저장
    @PostMapping("/amount-save")
    public CommonResponseEntity<String> saveAmount(
            HttpSession session, @RequestBody SaveAmountRequest saveAmountRequest) {
        paymentFacade.saveAmount(session, saveAmountRequest);
        return CommonResponseEntity.success("Payment temp save successful");
    }

    // 결제 금액 검증
    @PostMapping("/amount-verify")
    public CommonResponseEntity<String> verifyAmount(
            HttpSession session, @RequestBody SaveAmountRequest saveAmountRequest) {
        boolean isValid = paymentFacade.verifyAmount(session, saveAmountRequest);
        if (isValid) {
            return CommonResponseEntity.success("Payment is valid");
        } else {
            return CommonResponseEntity.error(HttpStatus.BAD_REQUEST, "금액이 다릅니다");
        }
    }

    // 결제 승인 요청
    @PostMapping("/confirm")
    public CommonResponseEntity<?> confirmPayment(
            @RequestBody ConfirmPaymentRequest confirmPaymentRequest) {
        try {
            PaymentResponse paymentResponse = paymentFacade.confirmPayment(confirmPaymentRequest);

            // 상태 확인 및 응답 구성
            if (paymentResponse == null || paymentResponse.getStatus() == null) {
                return CommonResponseEntity.error(
                        HttpStatus.BAD_REQUEST,
                        "Invalid response from Toss API. Please try again.");
            }

            if ("DONE".equals(paymentResponse.getStatus())) {
                return CommonResponseEntity.success(paymentResponse);
            } else {
                return CommonResponseEntity.error(
                        HttpStatus.BAD_REQUEST,
                        "Payment failed with status: " + paymentResponse.getStatus());
            }
        } catch (Exception e) {
            return CommonResponseEntity.error(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Error: " + e.getMessage());
        }
    }

    // 서버에서 orderId 및 customerKey 생성 및 반환
    @GetMapping("/keys")
    public CommonResponseEntity<GenerateKeyResponse> generateKeys() {
        // UUID 생성
        String orderId = UUID.randomUUID().toString();
        String customerKey = UUID.randomUUID().toString();

        // 응답 구성
        GenerateKeyResponse response = new GenerateKeyResponse(orderId, customerKey);
        return CommonResponseEntity.success(response);
    }
}
