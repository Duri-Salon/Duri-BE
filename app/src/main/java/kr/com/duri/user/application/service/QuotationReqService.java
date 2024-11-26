package kr.com.duri.user.application.service;

import kr.com.duri.groomer.domain.entity.Groomer;
import kr.com.duri.user.application.dto.response.NewQuotationReqDetailResponse;
import kr.com.duri.user.domain.entity.Request;

public interface QuotationReqService {
    NewQuotationReqDetailResponse getQuotationReqDetail(Request request, Groomer groomer);
}
