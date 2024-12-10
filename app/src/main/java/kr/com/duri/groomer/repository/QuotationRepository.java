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

    // 매장의 승인(APPROVED)된 현재로부터 가장 최근 견적서 조회
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

    // 사용자의 승인(APPROVED)된 다음 시술 조회
    @Query(
            """
    SELECT q FROM Quotation q
    JOIN q.request r
    JOIN r.quotation qr
    JOIN qr.pet p
    WHERE p.user.id = :userId
    AND q.status = 'APPROVED'
    AND q.startDateTime > :currentTime
    ORDER BY q.startDateTime ASC LIMIT 1
    """)
    Optional<Quotation> findApprovedNextQuotation(
            @Param("userId") Long userId, @Param("currentTime") LocalDateTime currentTime);

    // 사용자의 견적서 리스트 확인 : [매장 ID, 횟수]
    @Query(
            """
    SELECT r.shop.id, COUNT(q) FROM Quotation q
    JOIN q.request r
    JOIN r.quotation qr
    WHERE qr.pet.id = :petId
    GROUP BY r.shop.id
    HAVING COUNT(q) >= 3
    ORDER BY COUNT(q) DESC
    """)
    List<Object[]> findRegularShop(@Param("petId") Long petId);

    // 사용자의 견적서 리스트 확인
    @Query(
            """
      SELECT q FROM Quotation q
      JOIN q.request r
      JOIN r.quotation qr
      WHERE qr.pet.id = :petId
      """)
    List<Quotation> findQuotationsByPetId(@Param("petId") Long petId);

    List<Quotation> findByRequestIdInOrderByPriceAsc(List<Long> requestIds);
}
