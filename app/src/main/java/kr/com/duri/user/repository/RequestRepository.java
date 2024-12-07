package kr.com.duri.user.repository;

import java.util.List;
import java.util.Optional;

import kr.com.duri.user.domain.entity.Request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RequestRepository extends JpaRepository<Request, Long> {

    // 매장이 받은 대기 중인 견적 요청 조회
    @Query(
            "SELECT r FROM Request r "
                    + "JOIN FETCH r.quotation q "
                    + "JOIN FETCH q.pet p "
                    + "WHERE r.shop.id = :shopId AND r.status = 'WAITING'")
    List<Request> findNewRequestsByShopId(@Param("shopId") Long shopId);

    Optional<Request> findById(Long requestId);

    @Query(
            "SELECT r FROM Request r "
                    + "JOIN FETCH r.quotation q "
                    + "JOIN FETCH q.pet p "
                    + "WHERE r.shop.id = :shopId AND r.status = 'APPROVED'")
    List<Request> findApprovedRequestsByShopId(@Param("shopId") Long shopId);

    // 예약확정된 견적 요청서 리스트 가져오기
    @Query(
            "SELECT r FROM Quotation q "
                    + "JOIN q.request r "
                    + "JOIN r.shop s "
                    + "WHERE s.id = :shopId AND q.status = 'APPROVED' AND q.complete = false"
    )
    List<Request> findReservationQuotationsByShopId(@Param("shopId") Long shopId);

    // 시술 완료된 견적 요청서 리스트 가져오기
    @Query(
            "SELECT r FROM Quotation q "
                    + "JOIN q.request r "
                    + "JOIN r.shop s "
                    + "WHERE s.id = :shopId AND q.status = 'APPROVED' AND q.complete = true"
    )
    List<Request> findCompleteQuotationsByShopId(@Param("shopId") Long shopId);
}
