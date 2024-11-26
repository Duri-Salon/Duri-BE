package kr.com.duri.groomer.controller;

import java.util.List;

import kr.com.duri.common.response.CommonResponseEntity;
import kr.com.duri.groomer.application.dto.quotations.response.NewQuotationResponse;
import kr.com.duri.groomer.application.facade.QuotationFacade;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/groomer/quotations")
public class QuotationController {

    private final QuotationFacade quotationFacade;

    // 새로운 견적 요청서 리스트
    @GetMapping("/new")
    public CommonResponseEntity<List<NewQuotationResponse>> getNewRequests(
            @RequestParam Long shopId) {
        return CommonResponseEntity.success(quotationFacade.getNewRequests(shopId));
    }
}
