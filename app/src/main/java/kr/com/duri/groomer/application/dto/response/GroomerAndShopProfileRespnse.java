package kr.com.duri.groomer.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GroomerAndShopProfileRespnse {
    private GroomerProfileDetailResponse groomerProfileDetail;
    private ShopProfileDetailResponse shopProfileDetail;
}
