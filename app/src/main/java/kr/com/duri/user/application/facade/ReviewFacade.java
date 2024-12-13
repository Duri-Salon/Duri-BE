package kr.com.duri.user.application.facade;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import kr.com.duri.groomer.application.service.QuotationService;
import kr.com.duri.groomer.application.service.ShopImageService;
import kr.com.duri.groomer.application.service.ShopService;
import kr.com.duri.groomer.application.service.ShopTagService;
import kr.com.duri.groomer.domain.entity.Quotation;
import kr.com.duri.groomer.domain.entity.Shop;
import kr.com.duri.groomer.domain.entity.ShopImage;
import kr.com.duri.groomer.exception.ShopNotFoundException;
import kr.com.duri.user.application.dto.request.NewReviewRequest;
import kr.com.duri.user.application.dto.request.UpdateReviewRequest;
import kr.com.duri.user.application.dto.response.GetShopReviewDetailResponse;
import kr.com.duri.user.application.dto.response.HomePetInfoResponse;
import kr.com.duri.user.application.dto.response.QuotationPetReviewResponse;
import kr.com.duri.user.application.dto.response.ReviewResponse;
import kr.com.duri.user.application.dto.response.ShopInfoResponse;
import kr.com.duri.user.application.dto.response.UserReviewResponse;
import kr.com.duri.user.application.dto.response.UserReviewResponseList;
import kr.com.duri.user.application.mapper.ReviewMapper;
import kr.com.duri.user.application.mapper.UserHomeMapper;
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

    private final PetService petService;
    private final ShopService shopService;
    private final ShopTagService shopTagService;
    private final ShopImageService shopImageService;
    private final QuotationService quotationService;
    private final ReviewService reviewService;
    private final ReviewImageService reviewImageService;
    private final ReviewMapper reviewMapper;
    private final UserHomeMapper userHomeMapper;

    // 매장 조회
    private Shop getShop(Long shopId) {
        return shopService.findById(shopId);
    }

    // 리뷰로 반려견 조회
    private Pet getPetByReview(Review review) {
        return Optional.ofNullable(review.getRequest().getQuotation().getPet())
                .orElseThrow(() -> new PetNotFoundException("해당 반려견을 찾을 수 없습니다."));
    }

    // 요청으로 반려견 조회
    private Pet getPetByQuotation(Request request) {
        return Optional.ofNullable(request.getQuotation().getPet())
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
    public List<ReviewResponse> getReviewsByShopId(String token) {
        Long shopId = shopService.getShopIdByToken(token);
        Shop shop = getShop(shopId);
        // 1) 매장으로 리뷰 조회
        List<Review> reviewList = reviewService.getReviewsByShopId(shopId);
        if (reviewList.isEmpty()) { // 해당 리뷰 없음
            return Collections.emptyList();
        }
        return reviewList.stream()
                .map(
                        review -> {
                            // 1) 사용자
                            Pet pet = getPetByReview(review);
                            SiteUser user = pet.getUser();
                            String gender = userHomeMapper.translateGender(pet.getGender());
                            HomePetInfoResponse homePetInfoResponse =
                                    userHomeMapper.toHomePetInfoResponse(pet, gender);
                            // 2) 리뷰 이미지
                            ReviewImage reviewImage =
                                    reviewImageService.getReviewImageByReviewId(review.getId());
                            return reviewMapper.toReviewResponse(
                                    user, shop, review, reviewImage, homePetInfoResponse);
                        })
                .collect(Collectors.toList());
    }

    // 내가 쓴 후기 목록 조회 (고객)
    public UserReviewResponseList getReviewsByUserId(String token) {
        // 1) 반려견 조회
        Long userId = shopService.getShopIdByToken(token);
        Pet pet = petService.getPetByUserId(userId);
        SiteUser user = pet.getUser();
        // 2) 리뷰 목록 조회
        List<Review> reviewList = reviewService.getReviewsByPetId(pet.getId());
        if (reviewList.isEmpty()) { // 리뷰 없음
            return reviewMapper.toUserReviewResponseList(0, Collections.emptyList());
        }
        List<UserReviewResponse> userReviewResponseList =
                reviewList.stream()
                        .map(
                                review -> {
                                    // 3) 리뷰 이미지
                                    ReviewImage reviewImage =
                                            reviewImageService.getReviewImageByReviewId(
                                                    review.getId());
                                    // 4) 매장 조회
                                    Shop shop = getShopByReview(review);
                                    return reviewMapper.toUserReviewResponse(
                                            user, review, reviewImage, shop);
                                })
                        .collect(Collectors.toList());
        // 4) 목록 DTO 변환
        int reviewCnt = reviewList.size();
        return reviewMapper.toUserReviewResponseList(reviewCnt, userReviewResponseList);
    }

    // 단일 리뷰 조회 (고객)
    public ReviewResponse getReview(Long reviewId) {
        Review review = reviewService.getReview(reviewId);
        // 1) 사용자
        Pet pet = getPetByReview(review);
        SiteUser user = pet.getUser();
        // 2) 매장
        Shop shop = getShopByReview(review);
        // 3) ReviewImage
        ReviewImage reviewImage = reviewImageService.getReviewImageByReviewId(review.getId());
        // 4) 반려견 정보
        String gender = userHomeMapper.translateGender(pet.getGender());
        HomePetInfoResponse homePetInfoResponse = userHomeMapper.toHomePetInfoResponse(pet, gender);
        return reviewMapper.toReviewResponse(user, shop, review, reviewImage, homePetInfoResponse);
    }

    // 리뷰 작성 - 견적서로 매장, 반려견 조회 (고객)
    public QuotationPetReviewResponse getPetReviewByQuotationId(Long quotationId) {
        Quotation quotation = quotationService.findQuotationById(quotationId);
        Request request = getRequestByQuotation(quotation);
        Shop shop = getShopByRequest(request);
        // 1) 매장 이미지
        ShopImage shopImage = shopImageService.getMainShopImage(shop);
        // 2) 매장 태그
        List<String> shopTags = shopTagService.findTagsByShopId(shop.getId());
        ShopInfoResponse shopInfo = reviewMapper.toShopInfoResponse(shop, shopImage, shopTags);
        // 3) 반려견
        Pet pet = getPetByQuotation(request);
        String gender = userHomeMapper.translateGender(pet.getGender());
        HomePetInfoResponse petInfo = userHomeMapper.toHomePetInfoResponse(pet, gender);
        return reviewMapper.toQuotationPetReviewResponse(shopInfo, petInfo);
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

    // 리뷰 수정
    public void updateReview(
            Long reviewId, UpdateReviewRequest newReviewRequest, MultipartFile img) {
        // Review 수정
        Review afterReview = reviewService.updateReview(reviewId, newReviewRequest);
        reviewImageService.saveReviewImage(img, afterReview);
    }

    // 리뷰 삭제
    public void deleteReview(Long reviewId) {
        reviewImageService.deleteReview(reviewId); // ReviewImage 삭제
        reviewService.deleteReview(reviewId); // Review 삭제
    }

    // 리뷰 이미지 삭제
    public void deleteReviewImage(Long reviewId) {
        reviewImageService.deleteReview(reviewId); // ReviewImage 삭제
    }

    // 매장 리뷰 상세 리스트 조회 (매장)
    public List<GetShopReviewDetailResponse> getShopReviewByShopId(Long shopId) {
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
                            // 3. 리뷰로 사용자 조회 & 애완견 조회
                            SiteUser user = getPetByReview(review).getUser();
                            Pet pet = getPetByReview(review);
                            // 4. DTO 변환
                            return reviewMapper.toGetShopReviewDetailResponse(
                                    review, reviewImage, user, pet);
                        })
                .collect(Collectors.toList());
    }
}
