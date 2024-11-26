package kr.com.duri.groomer.application.facade;

import kr.com.duri.groomer.application.dto.quotations.response.NewQuotationResponse;
import kr.com.duri.groomer.application.service.QuotationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class QuotationFacade {

    private final QuotationService quotationService;

    public List<NewQuotationResponse> getNewRequests(Long shopId) {
        return quotationService.getNewRequests(shopId);
    }

}
