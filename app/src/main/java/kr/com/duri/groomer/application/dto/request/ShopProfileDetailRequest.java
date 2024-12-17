package kr.com.duri.groomer.application.dto.request;

import java.time.LocalTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonFormat;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShopProfileDetailRequest {
    private String phone; // 매장 전화번호

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime openTime; // 오픈 시간

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime closeTime; // 마감 시간

    private String info; // 매장 소개

    private String kakaoTalk; // 오픈채팅 링크

    private List<String> tags; // 태그 목록
}
