package kr.com.duri.groomer.controller;

import jakarta.validation.Valid;
import kr.com.duri.common.response.CommonResponseEntity;
import kr.com.duri.groomer.application.dto.request.GroomerDetailRequest;
import kr.com.duri.groomer.application.dto.response.GroomerAndShopProfileRespnse;
import kr.com.duri.groomer.application.dto.response.GroomerProfileDetailResponse;
import kr.com.duri.groomer.application.facade.GroomerProfileFacade;
import lombok.RequiredArgsConstructor;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/groomer/profile")
public class GroomerProfileController {

    private final GroomerProfileFacade groomerProfileFacade;

    @GetMapping("/{groomerId}")
    public CommonResponseEntity<GroomerAndShopProfileRespnse> getGroomerProfile(
            @PathVariable Long groomerId) {
        return CommonResponseEntity.success(groomerProfileFacade.getGroomerProfile(groomerId));
    }

    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CommonResponseEntity<GroomerProfileDetailResponse> createGroomerProfile(
            @RequestHeader("authorization_shop") String token,
            @RequestPart @Valid GroomerDetailRequest groomerDetailRequest,
            @RequestPart(value = "image", required = false) MultipartFile img) {
        return CommonResponseEntity.success(
                groomerProfileFacade.createGroomerProfile(token, groomerDetailRequest, img));
    }

    @PutMapping(value = "/{groomerId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CommonResponseEntity<GroomerProfileDetailResponse> updateGroomerProfile(
            @PathVariable Long groomerId,
            @RequestPart @Valid GroomerDetailRequest groomerDetailRequest,
            @RequestPart(value = "image", required = false) MultipartFile img) {
        return CommonResponseEntity.success(
                groomerProfileFacade.updateGroomerProfile(groomerId, groomerDetailRequest, img));
    }

    @DeleteMapping("/{groomerId}")
    public CommonResponseEntity<String> deleteGroomerProfile(@PathVariable Long groomerId) {
        groomerProfileFacade.deleteGroomerProfile(groomerId);
        return CommonResponseEntity.success("미용사 프로필 삭제에 성공했습니다.");
    }
}
