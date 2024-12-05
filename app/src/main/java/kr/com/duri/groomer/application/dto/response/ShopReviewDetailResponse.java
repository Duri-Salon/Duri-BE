package kr.com.duri.groomer.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShopReviewDetailResponse {
    private Long userId; // 사용자 ID
    private String userName; // 사용자 이름
    private String userImageURL; // 사용자 이미지 URL
    private Long reviewId; // 리뷰 ID
    private int rating; // 별점
    private String reviewImageURL; // 리뷰 이미지 URL
    private String comment; // 리뷰 내용
    private Long quotationReqId; // 견적 요청서 ID
    private String menu; // 미용 메뉴
    private String addMenu; // 추가 미용 메뉴
    private String specailMenu; // 스페셜 케어
    private String design; // 디자인 컷
}
