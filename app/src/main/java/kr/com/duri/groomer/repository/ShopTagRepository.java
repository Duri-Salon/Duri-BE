package kr.com.duri.groomer.repository;

import java.util.List;

import kr.com.duri.groomer.domain.entity.ShopTag;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ShopTagRepository extends JpaRepository<ShopTag, Long> {
    List<ShopTag> findByShopId(Long shopId);
}
