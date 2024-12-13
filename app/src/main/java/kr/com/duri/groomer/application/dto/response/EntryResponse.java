package kr.com.duri.groomer.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EntryResponse {
    private EntryShopResponse shop; // 매장 정보
    private EntryGroomerResponse groomer; // 매장주(미용사)
}
