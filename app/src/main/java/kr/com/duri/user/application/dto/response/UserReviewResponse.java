package kr.com.duri.user.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserReviewResponse {

    private Long userId; // 고객 ID
    private Long reviewId; // 리뷰 ID
    private String createdAt; // 등록일
    private Long shopId; // 매장 ID
    private String shopName; // 매장명
    private String reviewImageURL; // 리뷰 이미지 URL
}
