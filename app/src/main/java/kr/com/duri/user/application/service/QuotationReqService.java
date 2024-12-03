package kr.com.duri.user.application.service;

import kr.com.duri.user.domain.entity.QuotationReq;

public interface QuotationReqService {

    QuotationReq saveQuotationRequest(QuotationReq quotationReq);

    QuotationReq getQuotationReqById(Long quotationReqId);
}
