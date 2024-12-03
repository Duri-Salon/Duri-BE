package kr.com.duri.groomer.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShopDetailResponse {
    private Long id; // 매장 ID
    private String name; // 이름
    private String address; // 주소
    private String imageURL; // 매장 이미지 URL
    private String phone; // 매장 전화번호
}
