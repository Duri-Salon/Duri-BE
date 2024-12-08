package kr.com.duri.user.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistoryResponse {
    private Long quotationId; // 견적서 ID
    private boolean complete; // 미용 완료 여부
    private String groomerImageURL; // 디자이너 이미지
    private String groomerName; // 디자이너 이름
    private Long shopId; // 매장 ID
    private String shopName; // 매장 이름
    private String petName; // 반려견 이름
    private String day; // 요일
    private String startDate; // 미용 받은 시작 시간
}
