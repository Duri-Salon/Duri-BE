package kr.com.duri.groomer.application.service;

import kr.com.duri.groomer.application.dto.quotations.response.NewQuotationResponse;

import java.util.List;

public interface QuotationService {
    List<NewQuotationResponse> getNewRequests(Long shopId);
}

