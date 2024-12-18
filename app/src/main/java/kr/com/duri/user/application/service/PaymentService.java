package kr.com.duri.user.application.service;

import java.util.List;

import jakarta.servlet.http.HttpSession;
import kr.com.duri.groomer.application.dto.response.IncomeResponse;
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

    // 견적서로 찾기
    Payment findByQuotationId(Long id);

    // 최근 5달 매출액 조회
    List<IncomeResponse> getFiveMonthIncomes(Long shopId);

    // 원하는 달, 그 전달, 이번달 매출액 조회
    List<IncomeResponse> getIncomeByMonth(Long shopId, String selectedMonth);

    // 최근 7일 매출액 조회
    List<IncomeResponse> getWeekIncomes(Long shopId);
}
