package kr.com.duri.groomer.controller;

import java.util.List;

import jakarta.validation.Valid;
import kr.com.duri.common.response.CommonResponseEntity;
import kr.com.duri.groomer.application.dto.request.NewFeedbackRequest;
import kr.com.duri.groomer.application.dto.response.*;
import kr.com.duri.groomer.application.facade.FeedbackFacade;
import lombok.RequiredArgsConstructor;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/feedback")
public class FeedbackController {

    private final FeedbackFacade feedbackFacade;

    @PostMapping(value = "/{quotationId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE) // 매장만 사용
    public CommonResponseEntity<FeedbackDetailResponse> createNewFeedback(
            @RequestHeader("authorization_shop") String token,
            @PathVariable Long quotationId,
            @RequestPart @Valid NewFeedbackRequest newFeedbackRequest,
            @RequestPart(value = "image", required = false) List<MultipartFile> images) {
        return CommonResponseEntity.success(
                feedbackFacade.createNewFeedback(token, quotationId, newFeedbackRequest, images));
    }

    @PutMapping("/{feedbackId}") // 해당 매장만 사용
    public CommonResponseEntity<?> updatePortfolio(
            @RequestHeader("authorization_shop") String token,
            @PathVariable Long feedbackId,
            @RequestBody NewFeedbackRequest newFeedbackRequest) {
        feedbackFacade.updatePortfolio(token, feedbackId, newFeedbackRequest);
        return CommonResponseEntity.success();
    }

    @PutMapping("/remove/{feedbackId}") // 해당 매장만 사용
    public CommonResponseEntity<String> deletePortfolio(
            @RequestHeader("authorization_shop") String token,
            @PathVariable Long feedbackId) {
        feedbackFacade.removePortfolio(token, feedbackId);
        return CommonResponseEntity.success("포트폴리오가 삭제되었습니다.");
    }

    @GetMapping("/{groomerId}") // 매장, 사용자 모두 사용
    public CommonResponseEntity<List<PortfolioListResponse>> getPortfolioList(
            @PathVariable Long groomerId) {
        return CommonResponseEntity.success(feedbackFacade.getPortfolioList(groomerId));
    }

    @GetMapping("/detail/{feedbackId}") // 매장, 사용자 모두 사용
    public CommonResponseEntity<PortfolioDetailResponse> getFeedbackDetail(
            @PathVariable Long feedbackId) {
        return CommonResponseEntity.success(feedbackFacade.getPortfolioDetail(feedbackId));
    }

    @GetMapping("/diary/detail/{quotationId}") // 사용자 사용
    public CommonResponseEntity<DiaryDetailResponse> getDiaryDetail(
            @PathVariable Long quotationId) {
        return CommonResponseEntity.success(feedbackFacade.getDiaryDetail(quotationId));
    }

    @GetMapping("/data") // 사용자 사용
    public CommonResponseEntity<FeedbackDataResponse> getFeedbackData(
            @RequestHeader("authorization_user") String token) {
        return CommonResponseEntity.success(feedbackFacade.getFeedbackData(token));
    }
}
