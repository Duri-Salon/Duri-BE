package kr.com.duri.groomer.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import kr.com.duri.groomer.domain.entity.Quotation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface QuotationRepository extends JpaRepository<Quotation, Long> {
    boolean existsByRequestId(Long requestId);

    Optional<Quotation> findByRequestId(Long requestId);

    // 현재로부터 가장 최근이며, 승인(APPROVED)된 견적서 조회
    @Query(
            """
        SELECT q FROM Quotation q
        JOIN q.request r
        WHERE r.shop.id = :shopId
        AND q.status = 'APPROVED'
        AND q.endDateTime > :currentTime
        ORDER BY q.endDateTime ASC LIMIT 1
        """)
    Optional<Quotation> findApprovedClosetQuotation(
            @Param("shopId") Long shopId, @Param("currentTime") LocalDateTime currentTime);

    // 매장의 당일 시술 견적서 조회
    @Query(
            """
          SELECT q FROM Quotation q
          JOIN q.request r
          WHERE r.shop.id = :shopId
          AND DATE(q.startDateTime) = DATE(:currentTime)
          ORDER BY q.startDateTime ASC
          """)
    List<Quotation> findTodayQuotation(
            @Param("shopId") Long shopId, @Param("currentTime") LocalDateTime currentTime);


    // QuotationReq ID로 Quotation 목록 조회
    List<Quotation> findByRequest_Quotation_Id(Long quotationReqId);
}
