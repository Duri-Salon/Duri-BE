package kr.com.duri.groomer.application.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShopProfileDetailResponse {
    private Long id; // 매장 ID
    private String name; // 이름
    private String address; // 주소
    private String imageURL; // 매장 이미지 URL
    private String phone; // 매장 전화번호
    private String openTime; // 오픈 시간
    private String closeTime; // 마감 시간
    private String info; // 매장 소개
    private String kakaoTalk; // 오픈채팅 링크
    private Float rating; // 평점
    private List<String> tags; // 태그 목록
}
