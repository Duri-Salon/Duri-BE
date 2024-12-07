package kr.com.duri.user.application.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegularShopResponse {

    private Long petId; // 반려견 ID
    private String petName; // 반려견 이름
    private List<HomeShopResponse> homeShopList; // 단골 매장
}
