package kr.com.duri.groomer.repository;

import kr.com.duri.groomer.domain.entity.Shop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShopRepository extends JpaRepository<Shop, Long> {
    Optional<Shop> findBySocialId(String socialId);

    // shopId로 존재 여부를 확인하는 메서드
    boolean existsById(Long shopId);
}
