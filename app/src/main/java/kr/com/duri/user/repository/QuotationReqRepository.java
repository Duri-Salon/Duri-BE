package kr.com.duri.user.repository;

import java.util.List;
import java.util.Optional;

import kr.com.duri.user.domain.entity.QuotationReq;

import org.springframework.data.jpa.repository.JpaRepository;

public interface QuotationReqRepository extends JpaRepository<QuotationReq, Long> {
    List<QuotationReq> findByPetId(Long petId);

    Optional<QuotationReq> findTopByPetIdOrderByCreatedAtDesc(Long petId);
}
