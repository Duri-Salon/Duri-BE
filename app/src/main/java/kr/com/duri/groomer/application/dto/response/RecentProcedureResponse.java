package kr.com.duri.groomer.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecentProcedureResponse {
    @Builder.Default private Long petId = 0L; // 펫 ID
    @Builder.Default private String petName = ""; // 펫 이름
    @Builder.Default private String imageURL = ""; // 펫 이미지 URL
    @Builder.Default private String breed = ""; // 견종
    @Builder.Default private String gender = ""; // 성별
    @Builder.Default private Integer age = 0; // 나이
    @Builder.Default private Float weight = 0F; // 무게
    @Builder.Default private String memo = ""; // 메모 사항
    @Builder.Default private Long userId = 0L; // 사용자 ID
    @Builder.Default private String userPhone = ""; // 보호자 전화번호
    @Builder.Default private Long quotationId = 0L; // 견적서 ID
    @Builder.Default private String startTime = ""; // 미용 시작 시간 (YYYY-MM-DD HH:MM)
    @Builder.Default private boolean complete = false; // 미용 완료 여부
    @Builder.Default private Boolean isNow = null; // 현재 여부

    // 빈 객체 반환
    public static RecentProcedureResponse createEmpty() {
        return RecentProcedureResponse.builder().build();
    }
}
