package kr.com.duri.groomer.controller;

import java.util.List;

import kr.com.duri.common.response.CommonResponseEntity;
import kr.com.duri.groomer.application.dto.request.QuotationUpdateCompleteRequest;
import kr.com.duri.groomer.application.dto.response.HomeQuotationReqResponse;
import kr.com.duri.groomer.application.dto.response.RecentProcedureResponse;
import kr.com.duri.groomer.application.dto.response.TodayScheduleResponse;
import kr.com.duri.groomer.application.facade.GroomerHomeFacade;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/shop/home")
public class GroomerHomeController {

    private final GroomerHomeFacade homeFacade;

    // DURI-221 : 가까운 시술정보 조회
    @GetMapping("/closet")
    public CommonResponseEntity<RecentProcedureResponse> getRecentProcedure(
            @RequestHeader("authorization_user") String token) {
        return CommonResponseEntity.success(homeFacade.getRecentProcedure(token));
    }

    // DURI-265 : 당일 스케줄 조회
    @GetMapping("/schedule")
    public CommonResponseEntity<List<TodayScheduleResponse>> getTodaySchedule(
            @RequestHeader("authorization_user") String token) {
        return CommonResponseEntity.success(homeFacade.getTodaySchedule(token));
    }

    // DURI-266 : 미용 완료 여부 수정
    @PutMapping("/complete/{quotationId}")
    public CommonResponseEntity<String> updateComplete(
            @PathVariable Long quotationId,
            @RequestBody QuotationUpdateCompleteRequest quotationUpdateCompleteRequest) {
        homeFacade.updateComplete(quotationId, quotationUpdateCompleteRequest);
        return CommonResponseEntity.success("미용 완료 여부가 성공적으로 수정되었습니다.");
    }

    // DURI-325 : 받은 견적요청서 리스트 조회
    @GetMapping("/request")
    public CommonResponseEntity<List<HomeQuotationReqResponse>> getRequestList(
            @RequestHeader("authorization_user") String token) {
        return CommonResponseEntity.success(homeFacade.getRequestList(token));
    }
}
