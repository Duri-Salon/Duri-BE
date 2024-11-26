package kr.com.duri.user.repository;

import java.util.List;
import java.util.Optional;

import kr.com.duri.user.domain.entity.Request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {

    @Query(
            "SELECT r FROM Request r "
                    + "JOIN FETCH r.quotation q "
                    + "JOIN FETCH q.pet p "
                    + "WHERE r.shop.id = :shopId AND r.status = 'WAITING'")
    List<Request> findNewRequestsByShopId(@Param("shopId") Long shopId);

    Optional<Request> findById(Long requestId);
}
