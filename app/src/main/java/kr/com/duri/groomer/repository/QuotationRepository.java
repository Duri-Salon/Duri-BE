package kr.com.duri.groomer.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
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

    @Query(
            """
      SELECT q FROM Quotation q
      JOIN q.request r
      JOIN r.quotation qr
      WHERE qr.pet.id = :petId and q.noShow = true
      """)
    List<Quotation> findNoShowQuotationsByPetId(@Param("petId") Long petId);

    @Query(
            """
      SELECT q FROM Quotation q
      JOIN q.request r
      JOIN r.quotation qr
      WHERE qr.pet.id = :petId and r.status = 'APPROVED'
      """)
    List<Quotation> findApprovedQuotationsByPetId(@Param("petId") Long petId);

    // 나이별 통계 : List<기준, 개수>
    @Query(
            """
            SELECT
                CASE
                    WHEN p.age <= 3 THEN '~3세'
                    WHEN p.age BETWEEN 4 AND 7 THEN '3세~7세'
                    ELSE '7세~'
                END AS ageGroup,
                COUNT(p) AS count
            FROM Quotation q
            JOIN q.request r
            JOIN r.quotation qr
            JOIN qr.pet p
            WHERE r.shop.id = :shopId
            GROUP BY
                CASE
                    WHEN p.age <= 3 THEN '~3세'
                    WHEN p.age BETWEEN 4 AND 7 THEN '3세~7세'
                    ELSE '7세~'
                END
    """)
    List<Object[]> getPetAgeStatistics(@Param("shopId") Long shopId);

    // 질환별 통계 : List<기준, 개수>
    @Query(
            value =
                    """
        SELECT
           SUM(CASE WHEN p.pet_diseases LIKE '%disease1%' THEN 1 ELSE 0 END) AS 피부질환,
           SUM(CASE WHEN p.pet_diseases LIKE '%disease2%' THEN 1 ELSE 0 END) AS 귀질환,
           SUM(CASE WHEN p.pet_diseases LIKE '%disease3%' THEN 1 ELSE 0 END) AS 관절질환,
           SUM(CASE WHEN p.pet_diseases LIKE '%disease4%' THEN 1 ELSE 0 END) AS 기저질환,
           SUM(CASE WHEN p.pet_diseases LIKE '%disease5%' THEN 1 ELSE 0 END) AS 해당없음
        FROM quotation q
        JOIN request r ON q.request_id = r.request_id
        JOIN quotation_req qr ON r.quotation_req_id = qr.quotation_req_id
        JOIN pet p ON qr.pet_id = p.pet_id
        WHERE r.shop_id = :shopId
    """,
            nativeQuery = true)
    List<Map<String, Object>> getPetDiseaseCharacterStatistics(@Param("shopId") Long shopId);

    // 성격별 통계 : List<기준, 개수>
    @Query(
            value =
                    """
        SELECT
           SUM(CASE WHEN p.pet_character LIKE '%character1%' THEN 1 ELSE 0 END) AS 예민한,
           SUM(CASE WHEN p.pet_character LIKE '%character2%' THEN 1 ELSE 0 END) AS 낯가리는,
           SUM(CASE WHEN p.pet_character LIKE '%character3%' THEN 1 ELSE 0 END) AS 입질,
           SUM(CASE WHEN p.pet_character LIKE '%character4%' THEN 1 ELSE 0 END) AS 친화적,
           SUM(CASE WHEN p.pet_character LIKE '%character5%' THEN 1 ELSE 0 END) AS 얌전한,
           SUM(CASE WHEN p.pet_character LIKE '%character6%' THEN 1 ELSE 0 END) AS 겁많은
        FROM quotation q
        JOIN request r ON q.request_id = r.request_id
        JOIN quotation_req qr ON r.quotation_req_id = qr.quotation_req_id
        JOIN pet p ON qr.pet_id = p.pet_id
        WHERE r.shop_id = :shopId
    """,
            nativeQuery = true)
    List<Map<String, Object>> getPetCharacterStatistics(@Param("shopId") Long shopId);
}
