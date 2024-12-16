package kr.com.duri.groomer.application.mapper;

import java.util.List;

import kr.com.duri.groomer.application.dto.response.AgeStatisticsResponse;
import kr.com.duri.groomer.application.dto.response.BestStatisticsResponse;
import kr.com.duri.groomer.application.dto.response.CharacterStatisticsResponse;
import kr.com.duri.groomer.application.dto.response.DiseaseStatisticsResponse;
import kr.com.duri.groomer.application.dto.response.FiveMonthIncomeResponse;
import kr.com.duri.groomer.application.dto.response.IncomeResponse;
import kr.com.duri.groomer.application.dto.response.SelectMonthIncomeResponse;
import kr.com.duri.groomer.application.dto.response.StatisticsResponse;
import kr.com.duri.groomer.application.dto.response.WeekIncomeResponse;
import kr.com.duri.groomer.domain.entity.Shop;

import org.springframework.stereotype.Component;

@Component
public class StatisticsMapper {

    // Shop Entity, List<IncomeResponse> to FiveMonthIncomeResponse DTO
    public FiveMonthIncomeResponse toFiveMonthIncomeResponse(
            Shop shop, List<IncomeResponse> incomeMonthResponses, Float beforeRatio) {
        return FiveMonthIncomeResponse.builder()
                .shopId(shop.getId())
                .beforeRatio(beforeRatio)
                .incomeMonthList(incomeMonthResponses)
                .build();
    }

    // Shop Entity, List<IncomeResponse>, beforeRatio, nowRatio to FiveMonthIncomeResponse DTO
    public SelectMonthIncomeResponse toSelectMonthIncomeResponse(
            Shop shop,
            List<IncomeResponse> incomeMonthResponses,
            Float beforeRatio,
            Float nowRatio) {
        return SelectMonthIncomeResponse.builder()
                .shopId(shop.getId())
                .beforeRatio(beforeRatio)
                .nowRatio(nowRatio)
                .incomeMonthList(incomeMonthResponses)
                .build();
    }

    // Shop Entity, List<IncomeResponse> to FiveMonthIncomeResponse DTO
    public WeekIncomeResponse toWeekIncomeResponse(
            Shop shop, List<IncomeResponse> incomeMonthResponses) {
        return WeekIncomeResponse.builder()
                .shopId(shop.getId())
                .incomeMonthList(incomeMonthResponses)
                .build();
    }

    // (기준, 개수, 비율) Object[] to StatisticsResponse DTO
    public StatisticsResponse toStatisticsResponse(String standard, Long count, Float ratio) {
        return StatisticsResponse.builder().standard(standard).count(count).ratio(ratio).build();
    }

    // Shop Entity, List<StatisticsResponse> to AgeStatisticsResponse DTO
    public AgeStatisticsResponse toAgeStatisticsResponse(
            Shop shop, List<StatisticsResponse> statisticsResponse) {
        return AgeStatisticsResponse.builder()
                .shopId(shop.getId())
                .ageList(statisticsResponse)
                .build();
    }

    // Shop Entity, List<StatisticsResponse> to DiseaseStatisticsResponse DTO
    public DiseaseStatisticsResponse toDiseaseStatisticsResponse(
            Shop shop, List<StatisticsResponse> statisticsResponse) {
        return DiseaseStatisticsResponse.builder()
                .shopId(shop.getId())
                .diseaseList(statisticsResponse)
                .build();
    }

    // Shop Entity, List<StatisticsResponse> to CharacterStatisticsResponse DTO
    public CharacterStatisticsResponse toCharacterStatisticsResponse(
            Shop shop, List<StatisticsResponse> statisticsResponse) {
        return CharacterStatisticsResponse.builder()
                .shopId(shop.getId())
                .characterList(statisticsResponse)
                .build();
    }

    // Shop Entity, List<StatisticsResponse> to CharacterStatisticsResponse DTO
    public BestStatisticsResponse toBestStatisticsResponse(
            Shop shop, List<StatisticsResponse> statisticsResponse) {
        return BestStatisticsResponse.builder()
                .shopId(shop.getId())
                .bestList(statisticsResponse)
                .build();
    }
}
