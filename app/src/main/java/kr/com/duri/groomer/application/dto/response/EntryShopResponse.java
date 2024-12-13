package kr.com.duri.groomer.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EntryShopResponse {
    private Long shopId;
    private String shopName;
    private String shopAddress;
    private String shopPhone;
    private Double shopLat;
    private Double shopLon;
    private String businessRegistrationNumber;
    private String groomerLicenseNumber;
}
