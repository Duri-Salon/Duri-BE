package kr.com.duri.user.application.dto.response;

import java.util.Date;

import kr.com.duri.user.domain.Enum.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PetDetailResponse {

    private String image; // 반려견 사진 URL
    private String name; // 이름
    private Integer age; // 나이
    private Gender gender; // 성별
    private String breed; // 견종
    private Integer weight; // 몸무게
    private Boolean neutering; // 중성화 여부
    private Boolean biting; // 입질 여부
    private Date lastGrooming; // 마지막 미용 일자
}
