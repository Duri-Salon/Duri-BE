package kr.com.duri.groomer.controller;

import kr.com.duri.common.response.CommonResponseEntity;
import kr.com.duri.groomer.application.dto.request.QuotationRequest;
import kr.com.duri.groomer.application.dto.request.QuotationUpdateRequest;
import kr.com.duri.groomer.application.dto.response.QuotationDetailResponse;
import kr.com.duri.groomer.application.facade.QuotationFacade;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/quotation")
public class QuotationController {

    private final QuotationFacade quotationFacade;

    // 견적서 작성
    @PostMapping
    public CommonResponseEntity<String> createQuotation(
            @RequestBody QuotationRequest quotationRequest) {
        quotationFacade.createQuotation(quotationRequest);
        return CommonResponseEntity.success("견적서가 성공적으로 저장되었습니다.");
    }

    // 견적서 수정
    @PutMapping("/{quotationId}")
    public CommonResponseEntity<String> updateQuotation(
            @PathVariable Long quotationId,
            @RequestBody QuotationUpdateRequest quotationUpdateRequest) {
        quotationFacade.updateQuotation(quotationId, quotationUpdateRequest);
        return CommonResponseEntity.success("견적서가 성공적으로 수정되었습니다.");
    }

    // 견적 상세 조회
    @GetMapping("/{requestId}")
    public CommonResponseEntity<QuotationDetailResponse> getQuotationDetail(
            @PathVariable Long requestId) {
        return CommonResponseEntity.success(quotationFacade.getQuotationDetail(requestId));
    }
}
