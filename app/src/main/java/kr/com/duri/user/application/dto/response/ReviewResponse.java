package kr.com.duri.user.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewResponse {
    private Long userId; // 고객 ID
    private String userImageURL; // 고객 이미지 URL
    private String userName; // 고객 이름
    private Long reviewId; // 리뷰 ID
    private Integer rating; // 별점
    private String createdAt; // 등록일
    private Long shopId; // 매장 ID
    private String shopName; // 매장명
    private String comment; // 리뷰 내용
    private String imgUrl; // 리뷰 이미지 URL
    private HomePetInfoResponse petInfo; // 펫 정보
}
