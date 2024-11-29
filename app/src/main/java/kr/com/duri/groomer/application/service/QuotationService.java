package kr.com.duri.groomer.application.service;

import kr.com.duri.groomer.application.dto.request.QuotationRequest;
import kr.com.duri.groomer.application.dto.request.QuotationUpdateRequest;
import kr.com.duri.groomer.domain.entity.Quotation;
import kr.com.duri.user.domain.entity.Request;

public interface QuotationService {

    void saveQuotation(Quotation quotation);

    Quotation findQuotationById(Long quotationId);

    void updateQuotation(Quotation quotation);
}
