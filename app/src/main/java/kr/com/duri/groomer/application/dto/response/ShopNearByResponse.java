package kr.com.duri.groomer.application.dto.response;

import java.time.LocalTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShopNearByResponse {
    private Long shopId; // 매장 ID
    private String shopImage; // 매장 이미지 URL
    private String shopName; // 매장 이름
    private String shopAddress; // 매장 주소
    private Double shopLat; // 매장 위도
    private Double shopLon; // 매장 경도
    private String shopPhone; // 매장 전화번호
    private LocalTime shopOpenTime; // 매장 오픈시간
    private LocalTime shopCloseTime; // 매장 마감시간
    private Float shopRating; // 매장 평균 평점
    private Integer reviewCnt; // 리뷰 수
    private Integer distance; // 중심 위치와의 거리 (미터 단위)
    private String kakaoTalkUrl; // 카카오톡 URL
    private String shopInfo; // 매장 소개
    private List<String> tags; // 태그 리스트
}
