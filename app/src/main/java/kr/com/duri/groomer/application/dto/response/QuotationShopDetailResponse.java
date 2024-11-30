package kr.com.duri.groomer.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuotationShopDetailResponse {

    private String shopName; // 매장이름
    private String shopAddress; // 매장 주소
    private String shopPhone; // 매장 전화번호
    private String groomerName; // 디자이너 이름
}
