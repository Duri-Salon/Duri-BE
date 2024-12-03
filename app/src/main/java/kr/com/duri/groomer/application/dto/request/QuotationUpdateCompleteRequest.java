package kr.com.duri.groomer.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuotationUpdateCompleteRequest {
    private boolean complete; // 시술 완료 여부
}
