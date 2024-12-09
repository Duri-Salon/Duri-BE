package kr.com.duri.user.application.facade;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import kr.com.duri.groomer.application.dto.response.ShopReviewDetailResponse;
import kr.com.duri.groomer.application.dto.response.ShopReviewResponse;
import kr.com.duri.groomer.application.service.GroomerService;
import kr.com.duri.groomer.application.service.QuotationService;
import kr.com.duri.groomer.application.service.ShopService;
import kr.com.duri.groomer.domain.entity.Groomer;
import kr.com.duri.groomer.domain.entity.Quotation;
import kr.com.duri.groomer.domain.entity.Shop;
import kr.com.duri.groomer.exception.ShopNotFoundException;
import kr.com.duri.user.application.dto.request.NewReviewRequest;
import kr.com.duri.user.application.dto.request.UpdateReviewRequest;
import kr.com.duri.user.application.dto.response.ReviewResponse;
import kr.com.duri.user.application.dto.response.UserReviewResponse;
import kr.com.duri.user.application.dto.response.UserReviewResponseList;
import kr.com.duri.user.application.mapper.ReviewMapper;
import kr.com.duri.user.application.service.PetService;
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
import kr.com.duri.user.exception.RequestNotFoundException;
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
    private final QuotationService quotationService;

    // 미용사 조회
    private Groomer getGroomer(Long shopId) {
        return groomerService.getGroomerByShopId(shopId);
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

    // 리뷰로 매장 조회
    private Shop getShopByReview(Review review) {
        return Optional.ofNullable(review.getRequest().getShop())
                .orElseThrow(() -> new ShopNotFoundException("해당 매장을 찾을 수 없습니다."));
    }

    // 견적으로 요청 조회
    private Request getRequestByQuotation(Quotation quotation) {
        return Optional.ofNullable(quotation.getRequest())
                .orElseThrow(() -> new RequestNotFoundException("해당 요청을 찾을 수 없습니다."));
    }

    // 요청으로 매장 조회
    private Shop getShopByRequest(Request request) {
        return Optional.ofNullable(request.getShop())
                .orElseThrow(() -> new ShopNotFoundException("해당 매장을 찾을 수 없습니다."));
    }

    // 매장 리뷰 리스트 조회 (매장)
    public List<ShopReviewResponse> getReviewsByShopId(Long shopId) {
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

    // 매장 리뷰 상세 리스트 조회 (매장)
    public List<ShopReviewDetailResponse> getReviewsDetailByShopId(Long shopId) {
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

    // 내가 쓴 후기 목록 조회 (고객)
    public UserReviewResponseList getReviewsByUserId(Long userId) {
        // 1. 반려견 조회
        Pet pet = petService.getPetByUserId(userId);
        // 2. 리뷰 목록 조회
        List<Review> reviewList = reviewService.getReviewsByPetId(pet.getId());
        if (reviewList.isEmpty()) { // 리뷰 없음
            return reviewMapper.toUserReviewResponseList(0, Collections.emptyList());
        }
        // 3. DTO 변환
        List<UserReviewResponse> userReviewResponseList =
                reviewList.stream()
                        .map(
                                review -> {
                                    // 4. 리뷰 이미지 조회
                                    ReviewImage reviewImage =
                                            reviewImageService.getReviewImageByReviewId(
                                                    review.getId());
                                    // 5. 고객 조회
                                    SiteUser user = getPetByReview(review).getUser();
                                    // 6. 매장 조회
                                    Shop shop = getShopByReview(review);
                                    return reviewMapper.toUserReviewResponse(
                                            user, review, reviewImage, shop);
                                })
                        .collect(Collectors.toList());
        // 4. 목록 DTO 변환
        int reviewCnt = reviewList.size();
        return reviewMapper.toUserReviewResponseList(reviewCnt, userReviewResponseList);
    }

    // 리뷰 작성 (고객)
    public boolean createReview(
            Long quotationId, NewReviewRequest newReviewRequest, MultipartFile img) {
        // 요청 조회
        Quotation quotation = quotationService.findQuotationById(quotationId);
        Request request = getRequestByQuotation(quotation);
        // Review 저장
        Review review = reviewMapper.toReview(newReviewRequest, request);
        if (review == null) {
            return false;
        }
        reviewService.createReview(review);
        // ReviewImage 저장
        reviewImageService.saveReviewImage(img, review);
        // 매장 평점 업데이트
        updateEverageRating(request, newReviewRequest.getRating());
        return true;
    }

    // 리뷰 추가에 따른 매장 평점 처리
    private void updateEverageRating(Request request, Integer newRating) {
        // 매장 조회
        Shop shop = getShopByRequest(request);
        Integer reviewCnt = reviewService.getReviewsByShopId(shop.getId()).size() - 1; // 매장 리뷰 수 확인
        Float originRating = shop.getRating(); // 기존 매장 평점
        // 새로운 평점 계산
        Float totalRating;
        if (reviewCnt == 0) { // 첫 평점이라면
            totalRating = Float.valueOf(newRating);
        } else {
            String formatRating =
                    String.format(
                            "%.1f", ((originRating * reviewCnt) + newRating) / (reviewCnt + 1));
            totalRating = Float.parseFloat(formatRating);
        }
        // 업데이트
        shopService.updateShopRating(shop.getId(), totalRating);
    }

    /* TODO : 화면 확정 시 하단 코드 변경 필요 */
    // 단일 리뷰 조회
    public ReviewResponse getReview(Long reviewId) {
        // Review 조회
        Review review = reviewService.getReview(reviewId);
        // ReviewImage 조회
        ReviewImage reviewImage = reviewImageService.getReviewImageByReviewId(review.getId());
        Groomer groomer = getGroomer(review.getRequest().getShop().getId());
        return reviewMapper.toReviewResponse(groomer, review, reviewImage);
    }

    // 리뷰 수정
    public boolean updateReview(
            Long reviewId, UpdateReviewRequest newReviewRequest, MultipartFile img) {
        // Review 수정
        Review afterReview = reviewService.updateReview(reviewId, newReviewRequest);
        if (afterReview == null) {
            return false;
        }
        reviewImageService.saveReviewImage(img, afterReview);
        return true;
    }

    // 리뷰 삭제
    public boolean deleteReview(Long reviewId) {
        // ReviewImage 삭제
        reviewImageService.deleteReview(reviewId);
        // Review 삭제
        reviewService.deleteReview(reviewId);
        return true;
    }
}
