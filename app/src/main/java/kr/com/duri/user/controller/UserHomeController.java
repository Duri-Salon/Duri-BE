package kr.com.duri.user.controller;

import kr.com.duri.common.response.CommonResponseEntity;
import kr.com.duri.user.application.dto.response.HomePetInfoResponse;
import kr.com.duri.user.application.dto.response.RecentProcedureResponse;
import kr.com.duri.user.application.dto.response.RegularShopResponse;
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

    // DURI-271 : 단골샵 조회
    @GetMapping("/regular/{userId}")
    public CommonResponseEntity<RegularShopResponse> getRegularInfo(@PathVariable Long userId) {
        RegularShopResponse regularShopResponses = userHomeFacade.getRegularShops(userId);
        return CommonResponseEntity.success(regularShopResponses);
    }

    // DURI-334 : 펫 간단 정보 조회
    @GetMapping("/pet/{userId}")
    public CommonResponseEntity<HomePetInfoResponse> getPetInfo(@PathVariable Long userId) {
        HomePetInfoResponse homePetInfoResponse = userHomeFacade.getPetInfo(userId);
        return CommonResponseEntity.success(homePetInfoResponse);
    }
}
