package kr.com.duri.groomer.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GroomerMyPageResponse {
    private GroomerProfileDetailResponse groomerProfileDetailResponse;
    private Integer reservationCount;
    private Integer noShowCount;
    private ShopProfileDetailResponse shopProfileDetailResponse;
}
