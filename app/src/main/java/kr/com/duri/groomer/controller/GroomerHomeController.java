package kr.com.duri.groomer.controller;

import java.util.List;

import kr.com.duri.common.response.CommonResponseEntity;
import kr.com.duri.groomer.application.dto.request.QuotationUpdateCompleteRequest;
import kr.com.duri.groomer.application.dto.response.RecentProcedureResponse;
import kr.com.duri.groomer.application.dto.response.ShopDetailResponse;
import kr.com.duri.groomer.application.dto.response.TodayScheduleResponse;
import kr.com.duri.groomer.application.facade.GroomerHomeFacade;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/home/shop")
public class GroomerHomeController {

    private final GroomerHomeFacade homeFacade;

    // DURI-260 : 매장정보 조회
    @GetMapping("/{shopId}")
    public CommonResponseEntity<ShopDetailResponse> getShopDetail(@PathVariable Long shopId) {
        return CommonResponseEntity.success(homeFacade.getShopDetail(shopId));
    }

    // DURI-221 : 가까운 시술정보 조회
    @GetMapping("/now/{shopId}")
    public CommonResponseEntity<RecentProcedureResponse> getRecentProcedure(
            @PathVariable Long shopId) {
        return CommonResponseEntity.success(homeFacade.getRecentProcedure(shopId));
    }

    // DURI-265 : 당일 스케줄 조회
    @GetMapping("/schedule/{shopId}")
    public CommonResponseEntity<List<TodayScheduleResponse>> getTodaySchedule(
            @PathVariable Long shopId) {
        return CommonResponseEntity.success(homeFacade.getTodaySchedule(shopId));
    }

    // DURI-266 : 미용 완료 여부 수정
    @PutMapping("/complete/{quotationId}")
    public CommonResponseEntity<String> updateComplete(
            @PathVariable Long quotationId,
            @RequestBody QuotationUpdateCompleteRequest quotationUpdateCompleteRequest) {
        homeFacade.updateComplete(quotationId, quotationUpdateCompleteRequest);
        return CommonResponseEntity.success("미용 완료 여부가 성공적으로 수정되었습니다.");
    }
}
