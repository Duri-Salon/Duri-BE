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
    public CommonResponseEntity<?> getGroomerProfile(@RequestParam Long groomerId) {
        return CommonResponseEntity.success("프로필 조회 성공");
    }

    @PostMapping()
    public CommonResponseEntity<GroomerProfileDetailResponse> createGroomerProfile(@RequestHeader String token, @RequestBody GroomerDetailRequest groomerDetailRequest) {
        try {
            return CommonResponseEntity.success(groomerProfileFacade.createGroomerProfile(token, groomerDetailRequest));
        } catch (Exception e) {
            return CommonResponseEntity.error(HttpStatus.BAD_REQUEST, "미용사 등록에 실패했습니다.");
        }
    }

    @PutMapping("/{groomerId}")
    public CommonResponseEntity<?> updateGroomerProfile(@RequestParam Long groomerId) {
        return CommonResponseEntity.success("프로필 수정 성공");
    }

    @DeleteMapping("/{groomerId}")
    public CommonResponseEntity<?> deleteGroomerProfile(@RequestParam Long groomerId) {
        return CommonResponseEntity.success("프로필 삭제 성공");
    }

}
