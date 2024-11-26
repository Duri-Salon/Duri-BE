package kr.com.duri.user.controller;

import java.util.List;

import kr.com.duri.common.response.CommonResponseEntity;
import kr.com.duri.user.application.dto.response.NewQuotationReqDetailResponse;
import kr.com.duri.user.application.dto.response.NewQuotationReqResponse;
import kr.com.duri.user.application.facade.QuotationReqFacade;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/quotation/request")
public class QuotationReqController {

    private final QuotationReqFacade quotationFacade;

    // 새로운 견적 요청서 리스트
    @GetMapping("/new")
    public CommonResponseEntity<List<NewQuotationReqResponse>> getNewRequests(
            @RequestParam Long shopId) {
        return CommonResponseEntity.success(quotationFacade.getNewRequests(shopId));
    }

    // 견적 요청 상세 정보
    @GetMapping("/{requestId}")
    public CommonResponseEntity<NewQuotationReqDetailResponse> getQuotationReqDetail(
            @PathVariable Long requestId) {
        return CommonResponseEntity.success(quotationFacade.getQuotationReqDetail(requestId));
    }
}
