package kr.com.duri.user.controller;

import java.util.List;

import kr.com.duri.common.response.CommonResponseEntity;
import kr.com.duri.user.application.dto.request.NewQuotationReqRequest;
import kr.com.duri.user.application.dto.request.QuotationReqDetailRequest;
import kr.com.duri.user.application.dto.response.*;
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
            @RequestHeader("authorization_shop") String token) {
        return CommonResponseEntity.success(quotationReqFacade.getNewRequests(token));
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
            @RequestHeader("authorization_shop") String token) {
        return CommonResponseEntity.success(quotationReqFacade.getApprovedRequests(token));
    }

    // 예약한 견적 요청서 리스트(Groomer)
    @GetMapping("/reservation")
    public CommonResponseEntity<List<ReservationQuotationReqResponse>> getReservationRequests(
            @RequestHeader("authorization_shop") String token) {
        return CommonResponseEntity.success(quotationReqFacade.getReservationRequests(token));
    }

    // 시술 완료한 견적 요청서 리스트(Groomer)
    @GetMapping("/reservation/complete")
    public CommonResponseEntity<List<ReservationQuotationReqResponse>> getCompleteRequests(
            @RequestHeader("authorization_shop") String token) {
        return CommonResponseEntity.success(quotationReqFacade.getCompleteRequests(token));
    }

    // 견적 요청서 작성(User)
    @PostMapping
    public CommonResponseEntity<Long> createQuotationRequest(
            @RequestBody NewQuotationReqRequest newQuotationReqRequest) {
        return CommonResponseEntity.success(
                quotationReqFacade.createQuotationRequest(newQuotationReqRequest));
    }

    //  견적 요청서 목록(User)
    @GetMapping("/user")
    public CommonResponseEntity<List<QuotationListResponse>> getQuotationReqsByUserId(
            @RequestHeader("authorization_user") String token) {
        return CommonResponseEntity.success(quotationReqFacade.getQuotationReqsByUserId(token));
    }

    // 견적 요청서 조회 및 견적서 순위
    @GetMapping("/detail")
    public CommonResponseEntity<QuotationReqDetailResponse> getQuotationReqDetail(
            @RequestParam Long quotationReqId, @RequestParam Double lat, @RequestParam Double lon) {
        QuotationReqDetailRequest request = new QuotationReqDetailRequest(quotationReqId, lat, lon);
        return CommonResponseEntity.success(quotationReqFacade.getQuotationReqDetail(request));
    }

    // 지난 견적 요청 상세 정보(User)
    @GetMapping("/last")
    public CommonResponseEntity<LastQuotationReqResponse> getLastQuotationReq(
            @RequestHeader("authorization_user") String token) {
        return CommonResponseEntity.success(quotationReqFacade.getLastQuotationReqDetail(token));
    }
}
