package kr.com.duri.groomer.application.facade;

import kr.com.duri.groomer.application.dto.request.QuotationRequest;
import kr.com.duri.groomer.application.service.QuotationService;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuotationFacade {

    private final QuotationService quotationService;

    public void createQuotation(QuotationRequest quotationRequest) {
        quotationService.saveQuotation(quotationRequest);
    }
}
