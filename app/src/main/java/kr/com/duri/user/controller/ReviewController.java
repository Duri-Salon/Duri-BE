package kr.com.duri.user.controller;

import java.util.List;

import jakarta.validation.Valid;
import kr.com.duri.common.response.CommonResponseEntity;
import kr.com.duri.user.application.dto.request.NewReviewRequest;
import kr.com.duri.user.application.dto.request.UpdateReviewRequest;
import kr.com.duri.user.application.dto.response.GetShopReviewDetailResponse;
import kr.com.duri.user.application.dto.response.QuotationPetReviewResponse;
import kr.com.duri.user.application.dto.response.ReviewResponse;
import kr.com.duri.user.application.dto.response.UserReviewResponseList;
import kr.com.duri.user.application.facade.ReviewFacade;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ReviewController {

    private final ReviewFacade reviewFacade;

    // DURI-294 : 매장 리뷰 리스트 조회 (미용사)
    @GetMapping("/shop/review")
    public CommonResponseEntity<List<ReviewResponse>> getShopReviewList(
            @RequestHeader("authorization_shop") String token) {
        return CommonResponseEntity.success(reviewFacade.getReviewsByShopId(token));
    }

    // DURI-288 : 내가 쓴 후기 목록 조회 (고객)
    @GetMapping("/user/review")
    public CommonResponseEntity<UserReviewResponseList> getUserReviewList(
            @RequestHeader("authorization_user") String token) {
        return CommonResponseEntity.success(reviewFacade.getReviewsByUserId(token));
    }

    // DURI-288 : 리뷰 상세 조회 (고객)
    @GetMapping("/review/{reviewId}")
    public CommonResponseEntity<ReviewResponse> getReview(@PathVariable Long reviewId) {
        return CommonResponseEntity.success(reviewFacade.getReview(reviewId));
    }

    // 매장 상세정보 조회(유저용)
    @GetMapping("/review/shop")
    public CommonResponseEntity<List<GetShopReviewDetailResponse>> getShopReviewByShopId(
            @RequestParam Long shopId) {
        return CommonResponseEntity.success(reviewFacade.getShopReviewByShopId(shopId));
    }

    // 리뷰 작성시 : 견적서로 매장,반려견 조회 (고객)
    @GetMapping("/user/review-new")
    public CommonResponseEntity<QuotationPetReviewResponse> getPetReviewByQuotationId(
            @RequestParam Long quotationId) {
        return CommonResponseEntity.success(reviewFacade.getPetReviewByQuotationId(quotationId));
    }

    // DURI-286 : 리뷰 작성 (고객)
    @PostMapping(value = "/user/review/new", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CommonResponseEntity<String> createReview(
            @RequestParam Long quotationId,
            @RequestPart @Valid NewReviewRequest newReviewRequest,
            @RequestPart(value = "image", required = false) MultipartFile img) {
        boolean success = reviewFacade.createReview(quotationId, newReviewRequest, img);
        return success
                ? CommonResponseEntity.success("리뷰가 성공적으로 저장되었습니다.")
                : CommonResponseEntity.error(
                        HttpStatus.BAD_REQUEST, "요청 데이터가 올바르지 않아 리뷰 작성에 실패하였습니다. 다시 시도해주세요.");
    }

    // DURI-289 : 리뷰 수정
    @PutMapping(value = "/review/{reviewId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CommonResponseEntity<String> updateReview(
            @PathVariable Long reviewId,
            @RequestPart @Valid UpdateReviewRequest updateReviewRequest,
            @RequestPart(value = "image", required = false) MultipartFile img) {
        reviewFacade.updateReview(reviewId, updateReviewRequest, img);
        return CommonResponseEntity.success("리뷰가 성공적으로 수정되었습니다.");
    }

    // DURI-290 : 리뷰 삭제
    @DeleteMapping("/review/{reviewId}")
    public CommonResponseEntity<String> deleteReview(@PathVariable Long reviewId) {
        reviewFacade.deleteReview(reviewId);
        return CommonResponseEntity.success("리뷰가 성공적으로 삭제되었습니다.");
    }

    // 리뷰 이미지 삭제 (고객)
    @DeleteMapping("/review-image/{reviewId}")
    public CommonResponseEntity<String> deleteReviewImage(@PathVariable Long reviewId) {
        reviewFacade.deleteReviewImage(reviewId);
        return CommonResponseEntity.success("리뷰 이미지가 성공적으로 삭제되었습니다.");
    }
}
