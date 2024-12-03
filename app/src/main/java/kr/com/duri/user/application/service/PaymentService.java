package kr.com.duri.user.application.service;

public interface PaymentService {

    // 월별 총 매출액 조회
    Long getTotalPriceMonth(Long shopId);
}
