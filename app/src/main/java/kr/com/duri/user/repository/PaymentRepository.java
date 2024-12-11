package kr.com.duri.user.repository;

import java.time.LocalDateTime;

import kr.com.duri.user.domain.entity.Payment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    // 이번달 총 매출액 조회
    @Query(
            value =
                    """
      SELECT SUM(p.price) FROM Payment p
      JOIN p.quotation q
      JOIN q.request r
      WHERE r.shop.id = :shopId
      AND p.status = 'SUCCESS'
      AND q.startDateTime BETWEEN :startOfMonth AND :endOfMonth
      """)
    Long findTotalPriceByShopId(
            @Param("shopId") Long shopId,
            @Param("startOfMonth") LocalDateTime startOfMonth,
            @Param("endOfMonth") LocalDateTime endOfMonth);

    Payment findByQuotationId(Long quotationId);
}
