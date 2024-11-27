package kr.com.duri.groomer.application.service;

import kr.com.duri.groomer.application.dto.request.QuotationRequest;
import kr.com.duri.user.domain.entity.Request;

public interface QuotationService {
    void saveQuotation(Request request, QuotationRequest quotationRequest);
}
