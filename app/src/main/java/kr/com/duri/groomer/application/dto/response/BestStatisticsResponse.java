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
public class BestStatisticsResponse {
    private Long shopId; // 매장 ID
    private List<StatisticsResponse> bestList; // 각 최다 키워드 통계 리스트
}
