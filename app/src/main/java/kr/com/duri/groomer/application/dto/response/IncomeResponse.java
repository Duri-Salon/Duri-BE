package kr.com.duri.groomer.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IncomeResponse {
    private String date; // 날짜
    private Long income; // 매출액

    public static IncomeResponse createResponse(String date, Long income) {
        return IncomeResponse.builder().date(date).income(income == null ? 0 : income).build();
    }
}
