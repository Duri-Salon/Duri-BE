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
public class MonthlyHistoryResponse {

    private String month; // 월
    private List<HistoryResponse> historyList; // 이용기록
}
