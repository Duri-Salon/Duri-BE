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
public class WeekIncomeResponse {
    private Long shopId; // 매장 ID
    private List<IncomeResponse> incomeMonthList; // 일별 매출액
}
