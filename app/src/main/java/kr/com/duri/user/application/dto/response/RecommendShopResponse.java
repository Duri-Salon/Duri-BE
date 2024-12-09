package kr.com.duri.user.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecommendShopResponse {
    private Long petId; // 반려견 ID
    private String recommendFeature; // 추천 특징
    private Long shopId; // 추천 매장 ID
    private String imageURL; // 매장 이미지 URL
    private String shopName; // 매장명
    private String address; // 주소
    private String phone; // 전화번호
    private String shopTag1; // 매장 태그1
    private String shopTag2; // 매장 태그2
    private Float score; // 추천 점수
}
