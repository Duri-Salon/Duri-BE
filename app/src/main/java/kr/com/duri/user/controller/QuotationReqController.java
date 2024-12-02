package kr.com.duri.user.controller;

import java.util.List;

import kr.com.duri.common.response.CommonResponseEntity;
import kr.com.duri.user.application.dto.request.NewQuotationReqRequest;
import kr.com.duri.user.application.dto.response.ApprovedQuotationReqResponse;
import kr.com.duri.user.application.dto.response.NewQuotationReqDetailResponse;
import kr.com.duri.user.application.dto.response.NewQuotationReqResponse;
import kr.com.duri.user.application.facade.QuotationReqFacade;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/quotation/request")
public class QuotationReqController {

    private final QuotationReqFacade quotationReqFacade;

    // 새로운 견적 요청서 리스트(Groomer)
    @GetMapping("/new")
    public CommonResponseEntity<List<NewQuotationReqResponse>> getNewRequests(
            @RequestParam Long shopId) {
        return CommonResponseEntity.success(quotationReqFacade.getNewRequests(shopId));
    }

    // 견적 요청 상세 정보(Groomer)
    @GetMapping("/{requestId}")
    public CommonResponseEntity<NewQuotationReqDetailResponse> getQuotationReqDetail(
            @PathVariable Long requestId) {
        return CommonResponseEntity.success(quotationReqFacade.getQuotationReqDetail(requestId));
    }

    // 답장한 견적 요청서 리스트(Groomer)
    @GetMapping("/approved")
    public CommonResponseEntity<List<ApprovedQuotationReqResponse>> getApprovedRequests(
            @RequestParam Long shopId) {
        return CommonResponseEntity.success(quotationReqFacade.getApprovedRequests(shopId));
    }

    // 견적 요청서 작성(User)
    @PostMapping
    public CommonResponseEntity<Long> createQuotationRequest(
            @RequestBody NewQuotationReqRequest newQuotationReqRequest) {
        return CommonResponseEntity.success(
                quotationReqFacade.createQuotationRequest(newQuotationReqRequest));
    }
}
