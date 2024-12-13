package kr.com.duri.user.application.service;

import java.time.LocalDateTime;
import java.util.List;

import kr.com.duri.user.domain.entity.QuotationReq;

public interface QuotationReqService {

    QuotationReq saveQuotationRequest(QuotationReq quotationReq);

    // TODO : 지워도 되는지 확인
    QuotationReq getQuotationReqById(Long quotationReqId);

    List<QuotationReq> findByPetId(Long petId);

    QuotationReq findLatestByPetId(Long petId);

    // 24시간 지난 요청서 중 close가 false인 것들을 찾는 메서드
    List<QuotationReq> findExpiredQuotationReqs(LocalDateTime thresholdTime);

    void closeQuotationReq(QuotationReq quotationReq);
}
