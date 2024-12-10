package kr.com.duri.groomer.application.dto.request;

import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonFormat;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShopDetailRequest {
    private String name; // 매장 이름
    private String phone; // 매장 전화번호
    private String address; // 매장 주소
    private Double lat; // 매장 위도
    private Double lon; // 매장 경도

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime openTime; // 오픈 시간

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime closeTime; // 마감 시간

    private String info; // 매장 소개
    private String kakaoTalk; // 오픈채팅 링크
}
