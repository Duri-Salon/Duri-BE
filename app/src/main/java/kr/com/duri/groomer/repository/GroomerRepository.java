package kr.com.duri.groomer.repository;

import java.util.List;

import kr.com.duri.groomer.domain.entity.Groomer;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GroomerRepository extends JpaRepository<Groomer, Long> {
    List<Groomer> findByShopId(Long shopId);
}
