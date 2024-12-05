package kr.com.duri.groomer.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShopReviewResponse {
    private Long reviewId; // 리뷰 ID
    private String reviewImageURL; // 리뷰 이미지 URL
    private String comment; // 리뷰 내용
    private Integer rating; // 별점
    private Long userId; // 고객 ID
    private String userImageURL; // 고객 이미지 URL
}
