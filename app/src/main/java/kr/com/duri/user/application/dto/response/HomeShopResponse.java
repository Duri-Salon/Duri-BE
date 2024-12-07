package kr.com.duri.user.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HomeShopResponse {
    private Long shopId; // 매장 ID
    private String imageURL; // 매장 이미지 URL
    private String shopName; // 매장 이름
    private Float rating; // 별점
    private Integer reviewCnt; // 리뷰 수
    private Integer visitCnt; // 방문 수
}
