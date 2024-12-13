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
public class CharacterStatisticsResponse {
    private Long shopId; // 매장 ID
    private List<StatisticsResponse> characterList; // 성격별 누적 통계 리스트
}
