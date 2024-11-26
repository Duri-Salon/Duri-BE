package kr.com.duri.groomer.application.service;

import java.util.List;

import kr.com.duri.groomer.application.dto.quotations.response.NewQuotationResponse;

public interface QuotationService {
    List<NewQuotationResponse> getNewRequests(Long shopId);
}
