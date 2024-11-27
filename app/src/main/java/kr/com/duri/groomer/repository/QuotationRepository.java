package kr.com.duri.groomer.repository;

import kr.com.duri.groomer.domain.entity.Quotation;

import org.springframework.data.jpa.repository.JpaRepository;

public interface QuotationRepository extends JpaRepository<Quotation, Long> {
    boolean existsByRequestId(Long requestId);
}
