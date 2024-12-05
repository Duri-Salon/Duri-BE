package kr.com.duri.user.controller;

import kr.com.duri.common.response.CommonResponseEntity;
import kr.com.duri.user.application.dto.response.NewUserJwtResponse;
import kr.com.duri.user.application.facade.UserAuthFacade;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class UserAuthController {

    private final UserAuthFacade userAuthFacade;

    @GetMapping("/user/token")
    public CommonResponseEntity<NewUserJwtResponse> requestNewUserToken(
            @RequestParam String providerId) {
        NewUserJwtResponse newUserJwtResponse = null;

        try {
            newUserJwtResponse = userAuthFacade.createNewUserJwt(providerId);
        } catch (IllegalArgumentException e) {
            return CommonResponseEntity.error(HttpStatus.BAD_REQUEST, "토큰 생성에 실패했습니다.");
        }

        return CommonResponseEntity.success(newUserJwtResponse);
    }
}
