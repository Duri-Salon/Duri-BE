package kr.com.duri.user.controller;

import kr.com.duri.common.response.CommonResponseEntity;
import kr.com.duri.user.application.dto.request.NewPetRequest;
import kr.com.duri.user.application.dto.response.PetDetailResponse;
import kr.com.duri.user.application.facade.UserInfoFacade;
import kr.com.duri.user.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserInfoController {

    private final UserInfoFacade userInfoFacade;

    @PostMapping("/pet") // 헤더에 사용자 정보(고유 소설 아이디, PK)가 있으니 별도의 파라미터는 생략
    public CommonResponseEntity<PetDetailResponse> getSuccessExample(
            @RequestHeader("authorization_user") String token,
            @RequestBody NewPetRequest newPetRequest) {
        try {
            return CommonResponseEntity.success(userInfoFacade.createNewPet(token, newPetRequest));
        } catch (UserNotFoundException e) {
            return CommonResponseEntity.error(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다.");
        }
    }
}
