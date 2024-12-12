package kr.com.duri.groomer.application.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetShopDetailResponse {
    private ShopNearByResponse shopDetail; // 가게 정보
    private GroomerProfileDetailResponse groomerProfileDetail; // 디자이너 정보
    private List<String> shopImages; // 가게 이미지들(메인 사진 빼고 나머지들)
}
