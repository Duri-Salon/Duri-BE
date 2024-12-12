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
public class AgeStatisticsResponse {
    private Long shopId; // 매장 ID
    private List<StatisticsResponse> ageList; // 나이별 누적 통계 리스트
}
