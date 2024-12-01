package kr.com.duri.user.application.facade;

import java.util.List;
import java.util.stream.Collectors;

import kr.com.duri.groomer.application.service.GroomerService;
import kr.com.duri.groomer.domain.entity.Groomer;
import kr.com.duri.user.application.dto.request.NewReviewRequest;
import kr.com.duri.user.application.dto.request.UpdateReviewRequest;
import kr.com.duri.user.application.dto.response.ReviewResponse;
import kr.com.duri.user.application.mapper.ReviewMapper;
import kr.com.duri.user.application.service.PetService;
import kr.com.duri.user.application.service.ReviewImageService;
import kr.com.duri.user.application.service.ReviewService;
import kr.com.duri.user.domain.entity.Pet;
import kr.com.duri.user.domain.entity.Review;
import kr.com.duri.user.domain.entity.ReviewImage;
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
    private final PetService petService;

    // 미용사 조회
    private Groomer getGroomer(Long shopId) {
        return groomerService.getGroomerByShopId(shopId);
    }

    // 펫 조회
    private Pet getPet(Long userId) {
        return petService.getPetByUserId(userId);
    }

    // [1] 리뷰 목록 조회
    public List<ReviewResponse> getReviewList(Long petId) {
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
                            Groomer groomer = getGroomer(review.getShop().getId());
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
        Groomer groomer = getGroomer(review.getShop().getId());
        return reviewMapper.toReviewResponse(groomer, review, reviewImage);
    }

    // [3] 리뷰 추가
    public boolean createReview(
            NewReviewRequest newReviewRequest,
            MultipartFile img) {
        // Pet, Groomer 조회
        Pet pet = getPet(newReviewRequest.getUserId());
        Groomer groomer = getGroomer(newReviewRequest.getShopId());
        // Review 저장
        Review review = reviewService.createReview(pet, groomer, newReviewRequest); // );
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
