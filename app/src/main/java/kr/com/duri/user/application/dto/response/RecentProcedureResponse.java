package kr.com.duri.user.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecentProcedureResponse {
    private Long petId; // 반려견 ID
    private Integer lastSinceDay; // 마지막 미용일로부터 지난일자
    @Builder.Default private Long shopId = 0L; // 매장 ID
    @Builder.Default private String imageURL = ""; // 매장 이미지 URL
    @Builder.Default private String name = ""; // 매장 이름
    @Builder.Default private String address = ""; // 주소
    @Builder.Default private String phone = ""; // 전화번호
    @Builder.Default private String kakaoURL = ""; // 카카오톡 URL
    @Builder.Default private Long quotationId = 0L; // 견적서 ID
    @Builder.Default private Integer reserveDday = -1; // 예약일 디데이
    @Builder.Default private String reservationDate = ""; // 예약일자
    @Builder.Default private Integer price = 0; // 가격
}
