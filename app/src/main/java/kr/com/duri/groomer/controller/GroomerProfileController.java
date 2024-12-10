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
    public CommonResponseEntity<GroomerProfileDetailResponse> getGroomerProfile(@RequestParam Long groomerId) {
        try {
            return CommonResponseEntity.success(groomerProfileFacade.getGroomerProfile(groomerId));
        } catch (Exception e) {
            return CommonResponseEntity.error(HttpStatus.NOT_FOUND, "미용사를 찾지 못했습니다.");
        }
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
    public CommonResponseEntity<GroomerProfileDetailResponse> updateGroomerProfile(@RequestParam Long groomerId, @RequestBody GroomerDetailRequest groomerDetailRequest) {
        try {
            return CommonResponseEntity.success(groomerProfileFacade.updateGroomerProfile(groomerId, groomerDetailRequest));
        } catch (Exception e) {
            return CommonResponseEntity.error(HttpStatus.BAD_REQUEST, "미용사 수정에 실패했습니다.");
        }
    }

    @DeleteMapping("/{groomerId}")
    public CommonResponseEntity<String> deleteGroomerProfile(@RequestParam Long groomerId) {
        try {
            groomerProfileFacade.deleteGroomerProfile(groomerId);
            return CommonResponseEntity.success("미용사 프로필 삭제에 성공했습니다.");
        } catch (Exception e) {
            return CommonResponseEntity.error(HttpStatus.NOT_FOUND, "미용사를 찾지 못했습니다.");
        }
    }

}
