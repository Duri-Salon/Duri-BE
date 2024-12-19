package kr.com.duri.user.controller;

import java.util.List;

import kr.com.duri.common.response.CommonResponseEntity;
import kr.com.duri.user.application.dto.response.HomePetInfoResponse;
import kr.com.duri.user.application.dto.response.RecentProcedureResponse;
import kr.com.duri.user.application.dto.response.RecommendShopResponse;
import kr.com.duri.user.application.dto.response.RegularShopResponse;
import kr.com.duri.user.application.facade.UserHomeFacade;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user/home")
public class UserHomeController {

    private final UserHomeFacade userHomeFacade;

    // DURI-275 : 마지막 미용일자 및 최근 예약정보 조회
    @GetMapping("/schedule")
    public CommonResponseEntity<RecentProcedureResponse> getReservationInfo(
            @RequestHeader("authorization_user") String token) {
        RecentProcedureResponse recentProcedureResponse = userHomeFacade.getRecentProcedure(token);
        return CommonResponseEntity.success(recentProcedureResponse);
    }

    // DURI-271 : 단골샵 조회
    @GetMapping("/regular")
    public CommonResponseEntity<RegularShopResponse> getRegularInfo(
            @RequestHeader("authorization_user") String token) {
        RegularShopResponse regularShopResponses = userHomeFacade.getRegularShops(token);
        return CommonResponseEntity.success(regularShopResponses);
    }

    // DURI-334 : 펫 간단 정보 조회
    @GetMapping("/pet")
    public CommonResponseEntity<HomePetInfoResponse> getPetInfo(
            @RequestHeader("authorization_user") String token) {
        HomePetInfoResponse homePetInfoResponse = userHomeFacade.getPetInfo(token);
        return CommonResponseEntity.success(homePetInfoResponse);
    }

    // DURI-329 : 추천 매장 조회 (로그인 O)
    @GetMapping("/recommend")
    public CommonResponseEntity<List<RecommendShopResponse>> getRecommendShops(
            @RequestHeader("authorization_user") String token,
            @RequestParam Double lat,
            @RequestParam Double lon) {
        List<RecommendShopResponse> recommendShopResponses =
                userHomeFacade.getRecommendShops(token, lat, lon);
        return CommonResponseEntity.success(recommendShopResponses);
    }

    // DURI-329 : 추천 매장 조회 (로그인 X)
    @GetMapping("/recommend/non-member")
    public CommonResponseEntity<List<RecommendShopResponse>> getRecommendShopsNonMember(
            @RequestParam Double lat, @RequestParam Double lon) {
        List<RecommendShopResponse> recommendShopResponses =
                userHomeFacade.getNearByShopsDistance(lat, lon);
        return CommonResponseEntity.success(recommendShopResponses);
    }

    // DURI-281 : AI 스타일링
    @PostMapping(value = "/ai-styling", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CommonResponseEntity<String> applyGroomingStyle(
            @RequestParam String styleText,
            @RequestPart(value = "image", required = true) MultipartFile image) {
        try {
            return CommonResponseEntity.success(userHomeFacade.getPetAiStyling(styleText, image));
        } catch (Exception e) {
            return CommonResponseEntity.error(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "반려견 스타일링 이미지 생성 과정에 에러가 발생하였습니다. (" + e.getMessage() + ")");
        }
    }
}
