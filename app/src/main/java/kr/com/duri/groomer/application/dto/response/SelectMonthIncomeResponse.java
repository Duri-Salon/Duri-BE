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
public class SelectMonthIncomeResponse {
    private Long shopId; // 매장 ID
    private List<IncomeResponse> incomeMonthList; // 선택,이전,현재 월 매출액
    private Float beforeRatio; // 전월 대비 비율
    private Float nowRatio; // 이번달 대비 비율
}
