package kr.com.duri.user.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HomePetInfoResponse {
    private Long petId; // 반려견 ID
    private String imageURL; // 반려견 사진 URL
    private String name; // 이름
    private String breed; // 견종
    private String gender; // 성별
    private Integer age; // 나이
    private Float weight; // 몸무게
}
