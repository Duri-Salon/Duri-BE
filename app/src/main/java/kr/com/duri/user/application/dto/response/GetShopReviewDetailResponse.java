package kr.com.duri.user.application.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetShopReviewDetailResponse {
    private Long userId; // 사용자 ID
    private String userName; // 사용자 이름
    private String userImageURL; // 사용자 이미지 URL
    private Long reviewId; // 리뷰 ID
    private Integer rating; // 별점
    private String reviewImageURL; // 리뷰 이미지 URL
    private String comment; // 리뷰 내용
    private LocalDateTime createdAt; // 리뷰작성날짜
    private PetDetailResponse petDetail; // 애완견 정보
}
