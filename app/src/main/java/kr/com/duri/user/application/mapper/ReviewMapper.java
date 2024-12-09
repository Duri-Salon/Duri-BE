package kr.com.duri.user.application.mapper;

import java.time.format.DateTimeFormatter;
import java.util.List;

import kr.com.duri.groomer.application.dto.response.ShopReviewDetailResponse;
import kr.com.duri.groomer.application.dto.response.ShopReviewResponse;
import kr.com.duri.groomer.domain.entity.Groomer;
import kr.com.duri.groomer.domain.entity.Shop;
import kr.com.duri.user.application.dto.request.NewReviewRequest;
import kr.com.duri.user.application.dto.response.ReviewResponse;
import kr.com.duri.user.application.dto.response.UserReviewResponse;
import kr.com.duri.user.application.dto.response.UserReviewResponseList;
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

    // userReviewResponses Dto to toUserReviewResponseList DTO
    public UserReviewResponseList toUserReviewResponseList(
            Integer reviewCnt, List<UserReviewResponse> userReviewResponses) {
        return UserReviewResponseList.builder()
                .reviewCnt(reviewCnt)
                .reviewList(userReviewResponses)
                .build();
    }

    // Review Entity to toUserReviewResponse DTO
    public UserReviewResponse toUserReviewResponse(
            SiteUser user, Review review, ReviewImage reviewImage, Shop shop) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return UserReviewResponse.builder()
                .userId(user.getId())
                .userImageURL(safeGet(user.getImage()))
                .userName(safeGet(user.getName()))
                .reviewId(review.getId())
                .createdAt(
                        review.getCreatedAt() != null
                                ? review.getCreatedAt().format(dateFormatter)
                                : "")
                .rating(review.getRating())
                .shopId(shop.getId())
                .shopName(shop.getName())
                .comment(safeGet(review.getComment()))
                .reviewImageURL(safeGet(reviewImage.getImage()))
                .build();
    }

    // NewReviewRequest DTO to Entity
    public Review toReview(NewReviewRequest newReviewRequest, Request request) {
        return Review.builder()
                .rating(newReviewRequest.getRating())
                .comment(safeGet(newReviewRequest.getComment()))
                .request(request)
                .build();
    }

    /* 리팩토링 필요 */
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
}
