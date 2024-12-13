package kr.com.duri.user.application.mapper;

import java.time.format.DateTimeFormatter;
import java.util.List;

import kr.com.duri.common.Mapper.CommonMapper;
import kr.com.duri.groomer.domain.entity.Shop;
import kr.com.duri.groomer.domain.entity.ShopImage;
import kr.com.duri.user.application.dto.request.NewReviewRequest;
import kr.com.duri.user.application.dto.response.*;
import kr.com.duri.user.domain.entity.*;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReviewMapper {

    private final CommonMapper commonMapper;

    // null 방지
    private String safeGet(String value) {
        return value == null ? "" : value;
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
                .reviewId(review.getId())
                .createdAt(
                        review.getCreatedAt() != null
                                ? review.getCreatedAt().format(dateFormatter)
                                : "")
                .shopId(shop.getId())
                .shopName(shop.getName())
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

    // Review Entity to Response DTO
    public ReviewResponse toReviewResponse(
            SiteUser user,
            Shop shop,
            Review review,
            ReviewImage reviewImage,
            HomePetInfoResponse homePetInfoResponse) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return ReviewResponse.builder()
                .userId(user.getId())
                .userImageURL(safeGet(user.getImage()))
                .userName(user.getName())
                .reviewId(review.getId())
                .rating(review.getRating())
                .createdAt(
                        review.getCreatedAt() != null
                                ? review.getCreatedAt().format(dateFormatter)
                                : "")
                .shopId(shop.getId())
                .shopName(shop.getName())
                .comment(safeGet(review.getComment()))
                .imgUrl(safeGet(reviewImage.getImage()))
                .petInfo(homePetInfoResponse)
                .build();
    }

    // to QuotationPetReviewResponse DTO
    public QuotationPetReviewResponse toQuotationPetReviewResponse(
            ShopInfoResponse shopInfo, HomePetInfoResponse petInfo) {
        return QuotationPetReviewResponse.builder().shopInfo(shopInfo).petInfo(petInfo).build();
    }

    // to ShopInfoResponse DTO
    public ShopInfoResponse toShopInfoResponse(
            Shop shop, ShopImage shopImage, List<String> shopTagsStr) {
        return ShopInfoResponse.builder()
                .shopId(shop.getId())
                .imageURL(shopImage != null ? shopImage.getShopImageUrl() : "")
                .shopName(shop.getName())
                .address(safeGet(shop.getAddress()))
                .shopTag1(shopTagsStr.size() > 0 ? shopTagsStr.get(0) : "")
                .shopTag2(shopTagsStr.size() > 1 ? shopTagsStr.get(1) : "")
                .shopTag3(shopTagsStr.size() > 2 ? shopTagsStr.get(2) : "")
                .build();
    }

    // Review, ReviewImage, SiteUser Entity to ShopReviewResponse DTO
    public GetShopReviewDetailResponse toGetShopReviewDetailResponse(
            Review review, ReviewImage reviewImage, SiteUser user, Pet pet) {
        PetDetailResponse petDetail =
                PetDetailResponse.builder()
                        .image(pet.getImage())
                        .name(pet.getName())
                        .age(pet.getAge())
                        .gender(pet.getGender())
                        .breed(pet.getBreed())
                        .weight(pet.getWeight())
                        .neutering(pet.getNeutering())
                        .character(commonMapper.toListString(pet.getCharacter()))
                        .diseases(commonMapper.toListString(pet.getDiseases()))
                        .lastGrooming(pet.getLastGrooming())
                        .build();
        return GetShopReviewDetailResponse.builder()
                .userId(user.getId())
                .userName(user.getName())
                .userImageURL(safeGet(user.getImage()))
                .reviewId(review.getId())
                .rating(review.getRating())
                .reviewImageURL(safeGet(reviewImage.getImage()))
                .comment(safeGet(review.getComment()))
                .createdAt(review.getCreatedAt())
                .petDetail(petDetail)
                .build();
    }
}
