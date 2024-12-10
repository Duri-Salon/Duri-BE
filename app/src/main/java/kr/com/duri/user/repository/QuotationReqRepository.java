package kr.com.duri.user.repository;

import java.util.List;

import kr.com.duri.user.domain.entity.QuotationReq;

import org.springframework.data.jpa.repository.JpaRepository;

public interface QuotationReqRepository extends JpaRepository<QuotationReq, Long> {
    List<QuotationReq> findByPetId(Long petId);
}
