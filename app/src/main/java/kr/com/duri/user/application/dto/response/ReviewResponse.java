package kr.com.duri.user.application.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewResponse {

    private Long reviewId; // 리뷰 ID
    private String shopName; // 매장명
    private LocalDateTime createdAt; // 이용날짜
    private String groomerName; // 미용사명
    private Integer rating; // 별점
    private String comment; // 후기
    private String imgUrl; // 이미지 (1장)
}
