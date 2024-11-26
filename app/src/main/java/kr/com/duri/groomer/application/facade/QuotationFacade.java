package kr.com.duri.groomer.application.facade;

import java.util.List;

import kr.com.duri.groomer.application.dto.quotations.response.NewQuotationResponse;
import kr.com.duri.groomer.application.service.QuotationService;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuotationFacade {

    private final QuotationService quotationService;

    public List<NewQuotationResponse> getNewRequests(Long shopId) {
        return quotationService.getNewRequests(shopId);
    }
}
