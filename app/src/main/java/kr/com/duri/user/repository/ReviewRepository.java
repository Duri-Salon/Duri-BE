package kr.com.duri.user.repository;

import java.util.List;

import kr.com.duri.user.domain.entity.Review;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    // 반려견 ID 기준 리뷰 찾기
    List<Review> findByPetId(Long petId);
}
