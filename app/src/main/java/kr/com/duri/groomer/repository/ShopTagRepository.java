package kr.com.duri.groomer.repository;

import java.util.List;

import jakarta.transaction.Transactional;
import kr.com.duri.groomer.domain.entity.ShopTag;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

public interface ShopTagRepository extends JpaRepository<ShopTag, Long> {
    List<ShopTag> findByShopId(Long shopId);

    @Modifying
    @Transactional
    void deleteByShopId(Long shopid);
}
