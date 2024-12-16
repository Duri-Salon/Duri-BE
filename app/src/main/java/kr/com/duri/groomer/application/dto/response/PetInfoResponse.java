package kr.com.duri.groomer.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PetInfoResponse {
    private String name; // 반려견 이름
    private String breed; // 반려견 종
    private String gender; // 반려견 성별
    private Integer age; // 반려견 나이
    private Float weight; // 반려견 몸무게
    private Boolean neutralized; // 중성화 여부
    private String imageUrl; // 반려견 이미지 URL
}
