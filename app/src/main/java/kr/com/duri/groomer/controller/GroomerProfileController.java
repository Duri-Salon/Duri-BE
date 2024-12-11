package kr.com.duri.groomer.controller;

import kr.com.duri.common.response.CommonResponseEntity;
import kr.com.duri.groomer.application.dto.request.GroomerDetailRequest;
import kr.com.duri.groomer.application.dto.response.GroomerProfileDetailResponse;
import kr.com.duri.groomer.application.facade.GroomerProfileFacade;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/groomer/profile")
public class GroomerProfileController {

    private final GroomerProfileFacade groomerProfileFacade;

    @GetMapping("/{groomerId}")
    public CommonResponseEntity<GroomerProfileDetailResponse> getGroomerProfile(
            @PathVariable Long groomerId) {
            return CommonResponseEntity.success(groomerProfileFacade.getGroomerProfile(groomerId));
    }

    @PostMapping
    public CommonResponseEntity<GroomerProfileDetailResponse> createGroomerProfile(
            @RequestHeader("authorization_shop") String token, @RequestBody GroomerDetailRequest groomerDetailRequest) {
            return CommonResponseEntity.success(
                    groomerProfileFacade.createGroomerProfile(token, groomerDetailRequest));
    }

    @PutMapping("/{groomerId}")
    public CommonResponseEntity<GroomerProfileDetailResponse> updateGroomerProfile(
            @PathVariable Long groomerId, @RequestBody GroomerDetailRequest groomerDetailRequest) {
            return CommonResponseEntity.success(
                    groomerProfileFacade.updateGroomerProfile(groomerId, groomerDetailRequest));
    }

    @DeleteMapping("/{groomerId}")
    public CommonResponseEntity<String> deleteGroomerProfile(@PathVariable Long groomerId) {
            groomerProfileFacade.deleteGroomerProfile(groomerId);
            return CommonResponseEntity.success("미용사 프로필 삭제에 성공했습니다.");
    }
}
