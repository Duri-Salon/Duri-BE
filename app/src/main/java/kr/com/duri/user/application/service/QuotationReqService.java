package kr.com.duri.user.application.service;

import java.util.List;

import kr.com.duri.user.application.dto.response.NewQuotationReqDetailResponse;
import kr.com.duri.user.application.dto.response.NewQuotationReqResponse;

public interface QuotationReqService {
    List<NewQuotationReqResponse> getNewRequests(Long shopId);

    NewQuotationReqDetailResponse getQuotationReqDetail(Long requestId);
}
