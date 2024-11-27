package kr.com.duri.groomer.application.service;

import kr.com.duri.groomer.application.dto.request.QuotationRequest;

public interface QuotationService {
    void saveQuotation(QuotationRequest quotationRequest);
}
