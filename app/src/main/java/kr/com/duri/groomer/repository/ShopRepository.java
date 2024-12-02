package kr.com.duri.groomer.repository;

import kr.com.duri.groomer.domain.entity.Shop;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ShopRepository extends JpaRepository<Shop, Long> {

    // shopId로 존재 여부를 확인하는 메서드
    boolean existsById(Long shopId);
}
