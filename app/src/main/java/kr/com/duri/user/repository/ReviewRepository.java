package kr.com.duri.user.repository;

import java.util.List;

import kr.com.duri.user.domain.entity.Review;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    // 매장 ID 기준 리뷰 찾기
    @Query(
            value =
                    """
           SELECT rv FROM Review rv
           JOIN rv.request rq
           WHERE rq.shop.id = :shopId
           """)
    List<Review> findAllByShopId(@Param("shopId") Long shopId);

    // 반려견 ID 기준 리뷰 찾기
    @Query(
            value =
                    """
            select rv from Review rv
            join rv.request rq
            join rq.quotation qr
            where qr.pet.id = :petId
            """)
    List<Review> findByPetId(@Param("petId") Long petId);
}
