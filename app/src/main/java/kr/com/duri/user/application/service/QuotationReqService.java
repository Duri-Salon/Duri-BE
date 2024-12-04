package kr.com.duri.user.application.service;

import kr.com.duri.user.domain.entity.QuotationReq;

public interface QuotationReqService {

    QuotationReq saveQuotationRequest(QuotationReq quotationReq);

    // TODO : 지워도 되는지 확인
    QuotationReq getQuotationReqById(Long quotationReqId);
}
