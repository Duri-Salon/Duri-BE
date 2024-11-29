package kr.com.duri.groomer.application.facade;

import kr.com.duri.groomer.application.dto.request.QuotationRequest;
import kr.com.duri.groomer.application.dto.request.QuotationUpdateRequest;
import kr.com.duri.groomer.application.mapper.QuotationMapper;
import kr.com.duri.groomer.application.service.QuotationService;
import kr.com.duri.groomer.domain.entity.Quotation;
import kr.com.duri.user.application.service.QuotationReqService;
import kr.com.duri.user.domain.entity.Request;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuotationFacade {

    private final QuotationReqService requestService;
    private final QuotationService quotationService;
    private final QuotationMapper quotationMapper;

    public void createQuotation(QuotationRequest quotationRequest) {
        // 1. 요청 ID로 Request 객체 조회
        Request request = requestService.getRequestById(quotationRequest.getRequestId());

        // 2. Mapper를 사용하여 Quotation 엔티티 생성
        Quotation quotation = quotationMapper.toQuotationEntity(request, quotationRequest, quotationRequest.getPriceDetail());

        // 3. QuotationService를 사용하여 저장
        quotationService.saveQuotation(quotation);

        // 4. 요청 상태를 APPROVED로 업데이트
        requestService.updateRequestStatusToApproved(request.getId());
    }

    public void updateQuotation(Long quotationId, QuotationUpdateRequest quotationUpdateRequest) {
        // 1. 기존 견적서 엔티티 조회
        Quotation existingQuotation = quotationService.findQuotationById(quotationId);

        // 2. Mapper를 사용하여 엔티티 업데이트
        quotationMapper.updateQuotationEntity(existingQuotation, quotationUpdateRequest);

        // 3. 업데이트된 엔티티 저장
        quotationService.updateQuotation(existingQuotation);
    }
}

