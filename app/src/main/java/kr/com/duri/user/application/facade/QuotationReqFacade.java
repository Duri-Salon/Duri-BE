package kr.com.duri.user.application.facade;

import java.util.List;

import kr.com.duri.user.application.dto.response.NewQuotationReqDetailResponse;
import kr.com.duri.user.application.dto.response.NewQuotationReqResponse;
import kr.com.duri.user.application.service.QuotationReqService;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuotationReqFacade {

    private final QuotationReqService quotationService;

    public List<NewQuotationReqResponse> getNewRequests(Long shopId) {
        return quotationService.getNewRequests(shopId);
    }

    public NewQuotationReqDetailResponse getQuotationReqDetail(Long requestId) {
        return quotationService.getQuotationReqDetail(requestId);
    }
}
