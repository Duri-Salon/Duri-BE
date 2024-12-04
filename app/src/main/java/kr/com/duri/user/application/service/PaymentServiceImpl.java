package kr.com.duri.user.application.service;

import java.time.LocalDate;
import java.time.LocalDateTime;

import kr.com.duri.user.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;

    // 월별 총 매출액 조회
    public Long getTotalPriceMonth(Long shopId) {
        LocalDateTime startOfMonth = LocalDate.now().withDayOfMonth(1).atStartOfDay(); // 이번 달의 첫 초
        LocalDateTime endOfMonth = startOfMonth.plusMonths(1).minusSeconds(1); // 이번 달의 마지막 초
        return paymentRepository.findTotalPriceByShopId(shopId, startOfMonth, endOfMonth);
    }
}
