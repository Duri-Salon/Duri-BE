package kr.com.duri.user.application.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.servlet.http.HttpSession;
import kr.com.duri.groomer.application.dto.response.IncomeResponse;
import kr.com.duri.user.application.dto.request.ConfirmPaymentRequest;
import kr.com.duri.user.application.dto.request.SaveAmountRequest;
import kr.com.duri.user.application.service.PaymentService;
import kr.com.duri.user.domain.entity.Payment;
import kr.com.duri.user.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository; // 결제 데이터 관리
    private static final String CIRCUIT_BREAKER_NAME = "confirmPaymentCircuitBreaker";
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    // 결제 금액 임시 저장
    @Override
    public void saveAmount(HttpSession session, SaveAmountRequest saveAmountRequest) {
        session.setAttribute(
                String.valueOf(saveAmountRequest.getOrderId()), saveAmountRequest.getAmount());
    }

    // 결제 금액 검증
    @Override
    public boolean verifyAmount(HttpSession session, SaveAmountRequest saveAmountRequest) {
        Integer amount =
                (Integer) session.getAttribute(String.valueOf(saveAmountRequest.getOrderId()));
        return amount != null && amount.equals(saveAmountRequest.getAmount());
    }

    // Payment정보 DB저장
    @Override
    public Payment save(Payment payment) {
        return paymentRepository.save(payment);
    }

    // 결제 승인 요청 처리
    @Override
    @CircuitBreaker(name = CIRCUIT_BREAKER_NAME, fallbackMethod = "fallbackConfirmPayment")
    public JSONObject confirmPayment(
            ConfirmPaymentRequest confirmPaymentRequest,
            String tossApiUrl,
            String widgetSecretKey) {
        JSONObject response = new JSONObject();
        try {
            // 결제 요청 정보 JSON 형식으로 변환
            JSONObject requestData = new JSONObject();
            requestData.put("paymentKey", confirmPaymentRequest.getPaymentKey());
            requestData.put("orderId", confirmPaymentRequest.getOrderId());
            requestData.put("amount", confirmPaymentRequest.getAmount());

            // 인증 헤더 생성
            String authorization = generateAuthorizationHeader(widgetSecretKey);
            logger.info("Authorization Header: {}", authorization);
            logger.info("Request Data: {}", requestData.toJSONString());

            // Toss API 호출
            response = callTossApi(requestData, authorization, tossApiUrl);

        } catch (SocketTimeoutException e) {
            logger.error("Timeout occurred while waiting for Toss API response", e);
            response.put("status", "FAILED");
            response.put("message", "Timeout occurred");
        } catch (IOException e) {
            logger.error("IO error occurred during Toss API call", e);
            response.put("status", "FAILED");
            response.put("message", "IO error occurred");
        } catch (Exception e) {
            logger.error("Error during payment confirmation", e);
            response.put("status", "FAILED");
            response.put("message", "Unexpected error: " + e.getMessage());
        }

        // 기본값 확인 (Null-safe)
        if (!response.containsKey("status")) {
            response.put("status", "FAILED");
            response.put("message", "Invalid API response");
        }

        logger.info("Toss API Response: {}", response.toJSONString());
        return response;
    }

    // 서킷 브레이커 Open
    public JSONObject fallbackConfirmPayment(
            ConfirmPaymentRequest confirmPaymentRequest,
            String tossApiUrl,
            String widgetSecretKey,
            Throwable t) {
        System.out.println("Fallback method triggered dut to error");
        JSONObject fallbackResponse = new JSONObject();
        fallbackResponse.put("status", "FAILED");
        fallbackResponse.put(
                "message", "Service is temporarily unavailable, please try again later.");
        return fallbackResponse;
    }

    // 인증 헤더 생성
    private String generateAuthorizationHeader(String secretKey) {
        String credentials = secretKey + ":";
        return "Basic "
                + Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));
    }

    // Toss API 호출
    private JSONObject callTossApi(JSONObject requestData, String authorization, String tossApiUrl)
            throws IOException, ParseException {
        URL url = new URL(tossApiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setConnectTimeout(5000); // 연결 타임아웃
        connection.setReadTimeout(5000); // 응답 타임아웃
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", authorization);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        try (OutputStream os = connection.getOutputStream()) {
            os.write(requestData.toJSONString().getBytes(StandardCharsets.UTF_8));
        }

        int responseCode = connection.getResponseCode();
        InputStream is =
                responseCode == 200 ? connection.getInputStream() : connection.getErrorStream();
        JSONParser parser = new JSONParser();
        return (JSONObject) parser.parse(new InputStreamReader(is, StandardCharsets.UTF_8));
    }

    @Override
    public Payment findByQuotationId(Long id) {
        return paymentRepository.findByQuotationId(id);
    }

    // 최근 5달 매출액 조회
    @Override
    public List<IncomeResponse> getFiveMonthIncomes(Long shopId) {
        List<IncomeResponse> incomeResponses = new ArrayList<>();
        LocalDate currentMonth = LocalDate.now().withDayOfMonth(1); // 이번 달의 첫 날
        for (int i = 0; i < 5; i++) {
            LocalDateTime startOfMonth = currentMonth.atStartOfDay(); // 달의 첫초
            LocalDateTime endOfMonth = startOfMonth.plusMonths(1).minusSeconds(1); // 달의  마지막초
            Long income =
                    paymentRepository.findIncomeByShopIdAndDate(shopId, startOfMonth, endOfMonth);
            incomeResponses.add(
                    IncomeResponse.createResponse(
                            currentMonth.format(DateTimeFormatter.ofPattern("MM")), income));
            currentMonth = currentMonth.minusMonths(1); // 이전 달로 이동
        }
        Collections.reverse(incomeResponses);
        return incomeResponses;
    }

    // 원하는 달, 그 전달, 이번달 매출액 조회
    @Override
    public List<IncomeResponse> getIncomeByMonth(Long shopId, String selectedMonth) {
        List<IncomeResponse> incomeResponses = new ArrayList<>();
        LocalDate selectedDate;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        // LocalDate 파싱
        try {
            selectedDate =
                    LocalDate.parse(
                            selectedMonth.trim() + "-01",
                            DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            selectedDate = selectedDate.withDayOfMonth(1); // 선택한 달의 첫날
        } catch (Exception e) {
            throw new IllegalArgumentException("잘못된 날짜가 입력되었습니다.");
        }
        LocalDate[] months = {
            selectedDate.minusMonths(1), selectedDate, LocalDate.now().withDayOfMonth(1)
        }; // 이전 달, 선택한 달, 이번 달
        for (LocalDate month : months) {
            LocalDateTime startOfMonth = month.atStartOfDay(); // 시작 시점
            LocalDateTime endOfMonth = startOfMonth.plusMonths(1).minusSeconds(1); // 종료 시점
            Long income =
                    paymentRepository.findIncomeByShopIdAndDate(shopId, startOfMonth, endOfMonth);

            incomeResponses.add(IncomeResponse.createResponse(month.format(formatter), income));
        }
        return incomeResponses;
    }

    // 최근 7일 매출액 조회
    @Override
    public List<IncomeResponse> getWeekIncomes(Long shopId) {
        List<IncomeResponse> incomeResponses = new ArrayList<>();
        LocalDate currentDate = LocalDate.now(); // 이번 달의 첫 날
        for (int i = 0; i < 7; i++) {
            LocalDateTime startOfDay = currentDate.atStartOfDay(); // 일의 첫초
            LocalDateTime endOfDay = startOfDay.plusDays(1).minusSeconds(1); // 일의 마지막초
            Long income = paymentRepository.findIncomeByShopIdAndDate(shopId, startOfDay, endOfDay);
            incomeResponses.add(
                    IncomeResponse.createResponse(
                            currentDate.format(DateTimeFormatter.ofPattern("dd")), income));
            currentDate = currentDate.minusDays(1); // 이전 날로 이동
        }
        Collections.reverse(incomeResponses);
        return incomeResponses;
    }
}
