package kr.com.duri.groomer.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GroomerDetailResponse {

    private String image; // 사진
    private String name; // 이름
    private Integer history; // 경력 (년수)
    private String info; // 자기소개
}
