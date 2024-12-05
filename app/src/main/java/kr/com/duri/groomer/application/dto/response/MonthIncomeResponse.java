package kr.com.duri.groomer.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MonthIncomeResponse {
    private String month; // 이번달 (MM)
    private Long income; // 매출액
}
