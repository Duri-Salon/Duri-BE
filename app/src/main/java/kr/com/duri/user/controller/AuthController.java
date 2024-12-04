package kr.com.duri.user.controller;

import kr.com.duri.common.response.CommonResponseEntity;
import kr.com.duri.user.application.dto.response.NewJwtResponse;
import kr.com.duri.user.application.facade.AuthFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthFacade authFacade;

    @GetMapping("/token/request")
    public CommonResponseEntity<NewJwtResponse> requestToken(@RequestParam String providerId) {
        NewJwtResponse newJwtResponse = authFacade.createNewToken(providerId);
        return CommonResponseEntity.success(newJwtResponse);
    }
}
