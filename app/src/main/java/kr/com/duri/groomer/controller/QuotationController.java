package kr.com.duri.groomer.controller;

import kr.com.duri.common.response.CommonResponseEntity;
import kr.com.duri.groomer.application.dto.request.QuotationRequest;
import kr.com.duri.groomer.application.facade.QuotationFacade;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}

