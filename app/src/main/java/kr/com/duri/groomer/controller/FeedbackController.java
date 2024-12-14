package kr.com.duri.groomer.controller;

import jakarta.validation.Valid;
import kr.com.duri.common.response.CommonResponseEntity;
import kr.com.duri.groomer.application.dto.PortfolioDetailResponse;
import kr.com.duri.groomer.application.dto.request.NewFeedbackRequest;
import kr.com.duri.groomer.application.dto.response.FeedbackDetailResponse;
import kr.com.duri.groomer.application.dto.response.PortfolioListResponse;
import kr.com.duri.groomer.application.facade.FeedbackFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/feedback")
public class FeedbackController {

    private final FeedbackFacade feedbackFacade;

    @PostMapping(value = "/{quotationId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE) // 매장만 사용
    public CommonResponseEntity<FeedbackDetailResponse> createNewFeedback(
            @RequestHeader("authorization_shop") String token, @PathVariable Long quotationId,
            @RequestPart @Valid NewFeedbackRequest newFeedbackRequest,
            @RequestPart(value = "image", required = false) List<MultipartFile> images) {
        return CommonResponseEntity.success(feedbackFacade.createNewFeedback(token, quotationId, newFeedbackRequest, images));
    }

    @GetMapping("/{groomerId}") // 매장, 사용자 모두 사용
    public CommonResponseEntity<List<PortfolioListResponse>> getPortfolioList(@PathVariable Long groomerId) {
        return CommonResponseEntity.success(feedbackFacade.getPortfolioList(groomerId));
    }

    @GetMapping("/detail/{feedbackId}") // 매장, 사용자 모두 사용
    public CommonResponseEntity<PortfolioDetailResponse> getFeedbackDetail(@PathVariable Long feedbackId) {
        return CommonResponseEntity.success(feedbackFacade.getPortfolioDetail(feedbackId));
    }

}
