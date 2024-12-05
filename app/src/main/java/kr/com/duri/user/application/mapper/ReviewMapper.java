package kr.com.duri.user.application.mapper;

import kr.com.duri.groomer.application.dto.response.ShopReviewDetailResponse;
import kr.com.duri.groomer.application.dto.response.ShopReviewResponse;
import kr.com.duri.groomer.domain.entity.Groomer;
import kr.com.duri.user.application.dto.request.NewReviewRequest;
import kr.com.duri.user.application.dto.response.ReviewResponse;
import kr.com.duri.user.domain.entity.QuotationReq;
import kr.com.duri.user.domain.entity.Request;
import kr.com.duri.user.domain.entity.Review;
import kr.com.duri.user.domain.entity.ReviewImage;
import kr.com.duri.user.domain.entity.SiteUser;

import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {

    // null 방지
    private String safeGet(String value) {
        return value == null ? "" : value;
    }

    // Review Entity to Response DTO
    public ReviewResponse toReviewResponse(
            Groomer groomer, Review review, ReviewImage reviewImage) {
        return ReviewResponse.builder()
                .reviewId(review.getId())
                .shopName(safeGet(groomer.getShop().getName()))
                .groomerName(safeGet(groomer.getName()))
                .rating(review.getRating())
                .comment(safeGet(review.getComment()))
                .imgUrl(safeGet(reviewImage.getImage()))
                .createdAt(review.getCreatedAt())
                .build();
    }

    // Review Request DTO to Entity
    public Review toReview(NewReviewRequest newReviewRequest, Request request) {
        return Review.builder()
                .rating(newReviewRequest.getRating())
                .comment(safeGet(newReviewRequest.getComment()))
                .request(request)
                .build();
    }

    // Review, ReviewImage, SiteUser Entity to ShopReviewResponse DTO
    public ShopReviewResponse toShopReviewResponse(
            Review review, ReviewImage reviewImage, SiteUser user) {
        return ShopReviewResponse.builder()
                .reviewId(review.getId())
                .reviewImageURL(safeGet(reviewImage.getImage()))
                .comment(safeGet(review.getComment()))
                .rating(review.getRating())
                .userId(user.getId())
                .userImageURL(safeGet(user.getImage()))
                .build();
    }

    // Review, ReviewImage, SiteUser Entity to ShopReviewDetailResponse DTO
    public ShopReviewDetailResponse toShopReviewDetailResponse(
            Review review, ReviewImage reviewImage, QuotationReq quotationReq, SiteUser user) {
        return ShopReviewDetailResponse.builder()
                .userId(user.getId())
                .userName(safeGet(user.getName()))
                .userImageURL(safeGet(user.getImage()))
                .reviewId(review.getId())
                .rating(review.getRating())
                .comment(safeGet(review.getComment()))
                .reviewImageURL(safeGet(reviewImage.getImage()))
                .quotationReqId(quotationReq.getId())
                .menu(safeGet(quotationReq.getMenu()))
                .addMenu(safeGet(quotationReq.getAddMenu()))
                .specailMenu(safeGet(quotationReq.getSpecialMenu()))
                .design(safeGet(quotationReq.getDesign()))
                .build();
    }
}
