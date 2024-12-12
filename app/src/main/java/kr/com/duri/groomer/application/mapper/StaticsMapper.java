package kr.com.duri.groomer.application.mapper;

import java.util.List;

import kr.com.duri.groomer.application.dto.response.FiveMonthIncomeResponse;
import kr.com.duri.groomer.application.dto.response.IncomeResponse;
import kr.com.duri.groomer.application.dto.response.SelectMonthIncomeResponse;
import kr.com.duri.groomer.application.dto.response.WeekIncomeResponse;
import kr.com.duri.groomer.domain.entity.Shop;

import org.springframework.stereotype.Component;

@Component
public class StaticsMapper {

    // Shop Entity, List<IncomeResponse> to FiveMonthIncomeResponse DTO
    public FiveMonthIncomeResponse toFiveMonthIncomeResponse(
            Shop shop, List<IncomeResponse> incomeMonthResponses) {
        return FiveMonthIncomeResponse.builder()
                .shopId(shop.getId())
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
}
