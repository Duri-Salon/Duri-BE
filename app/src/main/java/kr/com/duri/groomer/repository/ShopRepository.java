package kr.com.duri.groomer.repository;

import kr.com.duri.groomer.domain.entity.Shop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShopRepository extends JpaRepository<Shop, Long> {
    Optional<Shop> findBySocialId(String socialId);
}
