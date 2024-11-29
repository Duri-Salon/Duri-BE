package kr.com.duri.groomer.application.service;

import kr.com.duri.groomer.domain.entity.Quotation;

public interface QuotationService {

    void saveQuotation(Quotation quotation);

    Quotation findQuotationById(Long quotationId);

    void updateQuotation(Quotation quotation);
}
