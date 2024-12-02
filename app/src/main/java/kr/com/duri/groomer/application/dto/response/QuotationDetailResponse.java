package kr.com.duri.groomer.application.dto.response;

import java.time.LocalDateTime;

import kr.com.duri.groomer.application.dto.request.QuotationRequest;
import kr.com.duri.user.application.dto.response.MenuDetailResponse;
import kr.com.duri.user.application.dto.response.PetDetailResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuotationDetailResponse {
    private QuotationShopDetailResponse shopDetail; // 매장 정보
    private LocalDateTime quotationCreatedAt; // 견적서 작성일
    private PetDetailResponse petDetail; // 반려견 정보
    private MenuDetailResponse menuDetail; // 메뉴 정보
    private QuotationRequest quotation; // 견적서 정보
}
