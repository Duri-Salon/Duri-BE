package kr.com.duri.groomer.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShopOnboardingInfo {
    private String name;
    private String phone;
    private String address; // 매장 주소
    private Double lat; // 매장 위도
    private Double lon; // 매장 경도
    private String businessRegistrationNumber; // 사업자 등록번호
    private String groomerLicenseNumber; // 미용사 면허번호
}
