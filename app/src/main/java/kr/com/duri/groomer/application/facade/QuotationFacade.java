package kr.com.duri.groomer.application.facade;

import kr.com.duri.groomer.application.dto.request.QuotationRequest;
import kr.com.duri.groomer.application.dto.request.QuotationUpdateRequest;
import kr.com.duri.groomer.application.service.QuotationService;
import kr.com.duri.user.application.service.RequestService;
import kr.com.duri.user.domain.entity.Request;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuotationFacade {

    private final RequestService requestService;
    private final QuotationService quotationService;

    public void createQuotation(QuotationRequest quotationRequest) {
        // 1. 요청 ID로 Request 객체 조회
        Request request = requestService.getRequestById(quotationRequest.getRequestId());

        // 2. QuotationService를 사용하여 견적서 저장
        quotationService.saveQuotation(request, quotationRequest);

        // 3. 요청 상태를 APPROVED로 업데이트
        requestService.updateRequestStatusToApproved(request.getId());
    }

    public void updateQuotation(Long quotationId, QuotationUpdateRequest quotationUpdateRequest) {
        // 수정 요청 Service에 전달
        quotationService.updateQuotation(quotationId, quotationUpdateRequest);
    }
}
