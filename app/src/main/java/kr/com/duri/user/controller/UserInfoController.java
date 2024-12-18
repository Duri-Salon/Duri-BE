package kr.com.duri.user.controller;

import java.util.List;

import jakarta.validation.Valid;
import kr.com.duri.common.response.CommonResponseEntity;
import kr.com.duri.user.application.dto.request.NewPetRequest;
import kr.com.duri.user.application.dto.response.*;
import kr.com.duri.user.application.facade.UserInfoFacade;
import lombok.RequiredArgsConstructor;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserInfoController {

    private final UserInfoFacade userInfoFacade;

    @PostMapping("/pet")
    public CommonResponseEntity<PetProfileResponse> createNewPet(
            @RequestHeader("authorization_user") String token,
            @RequestBody NewPetRequest newPetRequest) {
        return CommonResponseEntity.success(userInfoFacade.createNewPet(token, newPetRequest));
    }

    @GetMapping("/pet/{petId}")
    public CommonResponseEntity<PetProfileResponse> getPetDetail(@PathVariable Long petId) {
        return CommonResponseEntity.success(userInfoFacade.getPetDetail(petId));
    }

    @GetMapping("/pets")
    public CommonResponseEntity<PetProfileListResponse> getPetList(
            @RequestHeader("authorization_user") String token) {
        return CommonResponseEntity.success(userInfoFacade.getPetList(token));
    }

    @PutMapping(value = "/pet/{petId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CommonResponseEntity<PetProfileResponse> updatePetDetail(
            @PathVariable Long petId,
            @RequestPart @Valid NewPetRequest newPetRequest,
            @RequestPart(value = "image", required = false) MultipartFile img) {
        return CommonResponseEntity.success(userInfoFacade.updateNewPet(petId, newPetRequest, img));
    }

    // DURI-329 : 이용기록 조회
    @GetMapping("/history")
    public CommonResponseEntity<List<MonthlyHistoryResponse>> getHistory(
            @RequestHeader("authorization_user") String token) {
        List<HistoryResponse> historyResponseList = userInfoFacade.getHistoryList(token);
        List<MonthlyHistoryResponse> monthlyHistoryResponseList =
                userInfoFacade.getMonthlyHistory(historyResponseList);
        return CommonResponseEntity.success(monthlyHistoryResponseList);
    }

    @GetMapping("/profile")
    public CommonResponseEntity<SiteUserProfileResponse> getUserProfile(
            @RequestHeader("authorization_user") String token) {
        return CommonResponseEntity.success(userInfoFacade.getUserProfile(token));
    }

    @PutMapping(value = "/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CommonResponseEntity<String> updateUserProfile(
            @RequestHeader("authorization_user") String token,
            @RequestPart(value = "image", required = false) MultipartFile img) {
        userInfoFacade.updateUserProfile(token, img);
        return CommonResponseEntity.success("프로필 사진 수정이 완료되었습니다.");
    }

    @GetMapping(("/pet/info/{quotationId}"))
    public CommonResponseEntity<CustomerInfoResponse> getCustomerInfo(
            @PathVariable Long quotationId) {
        return CommonResponseEntity.success(userInfoFacade.getCustomerInfo(quotationId));
    }

    @PutMapping("/pet/delete/{petId}")
    public CommonResponseEntity<String> deletePet(@PathVariable Long petId) {
        userInfoFacade.deletePet(petId);
        return CommonResponseEntity.success("반려동물 정보가 삭제되었습니다.");
    }
}
