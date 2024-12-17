package kr.com.duri.groomer.application.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FiveMonthIncomeResponse {
    private Long shopId; // 매장 ID
    private Float beforeRatio; // 전월 대비
    private List<IncomeResponse> incomeMonthList; // 월별 매출액
}
