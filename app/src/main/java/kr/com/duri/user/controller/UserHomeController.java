package kr.com.duri.user.controller;

import kr.com.duri.common.response.CommonResponseEntity;
import kr.com.duri.user.application.dto.response.RecentProcedureResponse;
import kr.com.duri.user.application.facade.UserHomeFacade;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user/home")
public class UserHomeController {

    private final UserHomeFacade userHomeFacade;

    // DURI-275 : 마지막 미용일자 및 최근 예약정보 조회
    @GetMapping("/schedule/{userId}")
    public CommonResponseEntity<RecentProcedureResponse> getReservationInfo(
            @PathVariable Long userId) {
        RecentProcedureResponse recentProcedureResponse = userHomeFacade.getRecentProcedure(userId);
        return CommonResponseEntity.success(recentProcedureResponse);
    }
}
