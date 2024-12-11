package kr.com.duri.user.controller;

import java.util.List;

import jakarta.validation.Valid;
import kr.com.duri.common.response.CommonResponseEntity;
import kr.com.duri.user.application.dto.request.NewPetRequest;
import kr.com.duri.user.application.dto.response.*;
import kr.com.duri.user.application.facade.UserInfoFacade;
import kr.com.duri.user.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
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
        try {
            return CommonResponseEntity.success(userInfoFacade.createNewPet(token, newPetRequest));
        } catch (UserNotFoundException e) {
            return CommonResponseEntity.error(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다.");
        }
    }

    @GetMapping("/pet/{petId}")
    public CommonResponseEntity<PetProfileResponse> getPetDetail(@PathVariable Long petId) {
        try {
            return CommonResponseEntity.success(userInfoFacade.getPetDetail(petId));
        } catch (UserNotFoundException e) {
            return CommonResponseEntity.error(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다.");
        }
    }

    @GetMapping("/pets")
    public CommonResponseEntity<PetProfileListResponse> getPetList(
            @RequestHeader("authorization_user") String token) {
        try {
            return CommonResponseEntity.success(userInfoFacade.getPetList(token));
        } catch (UserNotFoundException e) {
            return CommonResponseEntity.error(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다.");
        }
    }

    @PutMapping(value = "/pet/{petId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CommonResponseEntity<PetProfileResponse> updatePetDetail(
            @PathVariable Long petId,
            @RequestPart @Valid NewPetRequest newPetRequest,
            @RequestPart(value = "image", required = false) MultipartFile img) {
        try {
            return CommonResponseEntity.success(
                    userInfoFacade.updateNewPet(petId, newPetRequest, img));
        } catch (UserNotFoundException e) {
            return CommonResponseEntity.error(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다.");
        }
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
}
