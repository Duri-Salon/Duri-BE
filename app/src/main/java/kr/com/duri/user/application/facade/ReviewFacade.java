package kr.com.duri.user.application.facade;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import kr.com.duri.groomer.application.dto.response.ShopReviewDetailResponse;
import kr.com.duri.groomer.application.dto.response.ShopReviewResponse;
import kr.com.duri.groomer.application.service.GroomerService;
import kr.com.duri.groomer.application.service.ShopService;
import kr.com.duri.groomer.domain.entity.Groomer;
import kr.com.duri.groomer.domain.entity.Shop;
import kr.com.duri.user.application.dto.request.NewReviewRequest;
import kr.com.duri.user.application.dto.request.UpdateReviewRequest;
import kr.com.duri.user.application.dto.response.ReviewResponse;
import kr.com.duri.user.application.mapper.ReviewMapper;
import kr.com.duri.user.application.service.PetService;
import kr.com.duri.user.application.service.RequestService;
import kr.com.duri.user.application.service.ReviewImageService;
import kr.com.duri.user.application.service.ReviewService;
import kr.com.duri.user.domain.entity.Pet;
import kr.com.duri.user.domain.entity.QuotationReq;
import kr.com.duri.user.domain.entity.Request;
import kr.com.duri.user.domain.entity.Review;
import kr.com.duri.user.domain.entity.ReviewImage;
import kr.com.duri.user.domain.entity.SiteUser;
import kr.com.duri.user.exception.PetNotFoundException;
import kr.com.duri.user.exception.QuotationReqNotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
public class ReviewFacade {

    private final ReviewImageService reviewImageService;
    private final ReviewService reviewService;
    private final ReviewMapper reviewMapper;
    private final GroomerService groomerService;
    private final ShopService shopService;
    private final PetService petService;
    private final RequestService requestService;

    // 미용사 조회
    private Groomer getGroomer(Long shopId) {
        return groomerService.getGroomerByShopId(shopId);
    }

    // 펫 조회
    private Pet getPet(Long userId) {
        return petService.getPetByUserId(userId);
    }

    // 매장 조회
    private Shop getShop(Long shopId) {
        return shopService.findById(shopId);
    }

    // 리뷰로 반려견 조회
    private Pet getPetByReview(Review review) {
        return Optional.ofNullable(review.getRequest().getQuotation().getPet())
                .orElseThrow(() -> new PetNotFoundException("해당 반려견을 찾을 수 없습니다."));
    }

    // 요청으로 견적 요청서 조회
    private QuotationReq getQuotationReqByRequest(Request request) {
        return Optional.ofNullable(request.getQuotation())
                .orElseThrow(() -> new QuotationReqNotFoundException("해당 견적 요청서를 찾을 수 없습니다."));
    }

    // 매장 리뷰 리스트 조회
    public List<ShopReviewResponse> getReviewByShop(Long shopId) {
        getShop(shopId);
        // 1. 매장으로 리뷰 조회
        List<Review> reviewList = reviewService.getReviewsByShopId(shopId);
        if (reviewList.isEmpty()) { // 해당 리뷰 없음
            return Collections.emptyList();
        }
        return reviewList.stream()
                .map(
                        review -> {
                            // 2. 리뷰로 리뷰 이미지 조회
                            ReviewImage reviewImage =
                                    reviewImageService.getReviewImageByReviewId(review.getId());
                            // 3. 리뷰로 사용자 조회
                            SiteUser user = getPetByReview(review).getUser();
                            // 4. DTO 변환
                            return reviewMapper.toShopReviewResponse(review, reviewImage, user);
                        })
                .collect(Collectors.toList());
    }

    // 매장 리뷰 상세 리스트 조회
    public List<ShopReviewDetailResponse> getReviewDetailByShop(Long shopId) {
        List<Review> reviewDetailList = reviewService.getReviewsByShopId(shopId);
        if (reviewDetailList.isEmpty()) { // 해당 리뷰 없음
            return Collections.emptyList();
        }
        return reviewDetailList.stream()
                .map(
                        review -> {
                            // 2. 리뷰로 리뷰 이미지 조회
                            ReviewImage reviewImage =
                                    reviewImageService.getReviewImageByReviewId(review.getId());
                            // 3. 리뷰로 사용자 조회
                            SiteUser user = getPetByReview(review).getUser();
                            // 4. 견적 요청서 조회
                            QuotationReq quotationReq =
                                    getQuotationReqByRequest(review.getRequest());
                            // 5. DTO 변환
                            return reviewMapper.toShopReviewDetailResponse(
                                    review, reviewImage, quotationReq, user);
                        })
                .collect(Collectors.toList());
    }

    /* TODO : 리뷰 엔티티 연관 변경에 따른 하단 코드 추후 리팩토링 필요 */
    // [1] 리뷰 목록 조회
    public List<ReviewResponse> getReviewList(Long userId) {
        // 반려견 ID 조회
        Long petId = getPet(userId).getId();
        // Review 목록 조회
        List<Review> reviewList = reviewService.getReviewList(petId);
        if (reviewList.isEmpty()) {
            // TODO : 리뷰 목록 비어있음
        }
        // ReviewImage 조회
        return reviewList.stream()
                .map(
                        review -> {
                            ReviewImage reviewImage =
                                    reviewImageService.getReviewImageByReviewId(review.getId());
                            Groomer groomer = getGroomer(review.getRequest().getShop().getId());
                            return reviewMapper.toReviewResponse(groomer, review, reviewImage);
                        })
                .collect(Collectors.toList());
    }

    // [2] 단일 리뷰 조회
    public ReviewResponse getReview(Long reviewId) {
        // Review 조회
        Review review = reviewService.getReview(reviewId);
        if (review == null) {
            // TODO : 해당하는 리뷰 없음
        }
        // ReviewImage 조회
        ReviewImage reviewImage = reviewImageService.getReviewImageByReviewId(review.getId());
        Groomer groomer = getGroomer(review.getRequest().getShop().getId());
        return reviewMapper.toReviewResponse(groomer, review, reviewImage);
    }

    // [3] 리뷰 추가
    public boolean createReview(NewReviewRequest newReviewRequest, MultipartFile img) {
        // Review 저장
        // TODO : Review Entity 수정에 의한 리팩토링 필요
        Request request = requestService.getRequestById(newReviewRequest.getRequestId());
        Review review = reviewMapper.toReview(newReviewRequest, request);
        if (review == null) {
            // TODO : 리뷰 저장 안됨
            return false;
        }
        // ReviewImage 저장
        reviewImageService.saveReviewImage(img, review);
        return true;
    }

    // [3] 리뷰 수정
    public boolean updateReview(
            Long reviewId, UpdateReviewRequest newReviewRequest, MultipartFile img) {
        // Review 수정
        Review afterReview = reviewService.updateReview(reviewId, newReviewRequest);
        if (afterReview == null) {
            // TODO : 리뷰 수정 안됨
            return false;
        }
        reviewImageService.saveReviewImage(img, afterReview);
        return true;
    }

    // [4] 리뷰 삭제
    public boolean deleteReview(Long reviewId) {
        // ReviewImage 삭제
        reviewImageService.deleteReview(reviewId);
        // Review 삭제
        reviewService.deleteReview(reviewId);
        return true;
    }
}
