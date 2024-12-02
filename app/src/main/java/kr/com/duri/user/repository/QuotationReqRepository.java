package kr.com.duri.user.repository;

import kr.com.duri.user.domain.entity.QuotationReq;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuotationReqRepository extends JpaRepository<QuotationReq, Long> {}
