package kr.com.duri.user.controller;

import java.util.List;

import jakarta.validation.Valid;
import kr.com.duri.common.response.CommonResponseEntity;
import kr.com.duri.user.application.dto.request.NewReviewRequest;
import kr.com.duri.user.application.dto.request.UpdateReviewRequest;
import kr.com.duri.user.application.dto.response.ReviewResponse;
import kr.com.duri.user.application.facade.ReviewFacade;
import lombok.RequiredArgsConstructor;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user/review")
public class ReviewController {

    private final ReviewFacade reviewFacade;

    // DURI-288 : 리뷰 전체 조회
    @GetMapping()
    public CommonResponseEntity<List<ReviewResponse>> getReviews(@RequestParam Long petId) {
        return CommonResponseEntity.success(reviewFacade.getReviewList(petId));
    }

    // DURI-288 : 리뷰 상세 조회
    @GetMapping("/{reviewId}")
    public CommonResponseEntity<ReviewResponse> getReview(@PathVariable Long reviewId) {
        return CommonResponseEntity.success(reviewFacade.getReview(reviewId));
    }

    // DURI-286 : 리뷰 작성
    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CommonResponseEntity<String> createReview(
            @RequestPart @Valid NewReviewRequest newReviewRequest,
            @RequestPart(value = "image", required = false) MultipartFile img) {
        // @RequestBody NewReviewRequest newReviewRequest) {
        // newReviewRequest.newImg(img);
        reviewFacade.createReview(newReviewRequest, img);
        return CommonResponseEntity.success("리뷰가 성공적으로 저장되었습니다.");
    }

    // DURI-289 : 리뷰 수정
    @PutMapping(value = "/{reviewId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CommonResponseEntity<String> updateReview(
            @PathVariable Long reviewId,
            @RequestPart @Valid UpdateReviewRequest updateReviewRequest,
            @RequestPart(value = "image", required = false) MultipartFile img) {
        // updateReviewRequest.newImg(img);
        reviewFacade.updateReview(reviewId, updateReviewRequest, img);
        return CommonResponseEntity.success("리뷰가 성공적으로 수정되었습니다.");
    }

    // DURI-290 : 리뷰 삭제
    @DeleteMapping("/{reviewId}")
    public CommonResponseEntity<String> deleteReview(@PathVariable Long reviewId) {
        reviewFacade.deleteReview(reviewId);
        return CommonResponseEntity.success("리뷰가 성공적으로 삭제되었습니다.");
    }
}
