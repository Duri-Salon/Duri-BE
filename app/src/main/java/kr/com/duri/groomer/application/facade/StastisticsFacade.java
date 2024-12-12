package kr.com.duri.groomer.application.facade;

import java.util.List;

import kr.com.duri.groomer.application.dto.response.FiveMonthIncomeResponse;
import kr.com.duri.groomer.application.dto.response.IncomeResponse;
import kr.com.duri.groomer.application.dto.response.SelectMonthIncomeResponse;
import kr.com.duri.groomer.application.dto.response.WeekIncomeResponse;
import kr.com.duri.groomer.application.mapper.StaticsMapper;
import kr.com.duri.groomer.application.service.ShopService;
import kr.com.duri.groomer.domain.entity.Shop;
import kr.com.duri.user.application.service.PaymentService;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StastisticsFacade {

    private static final Integer PREVIOUS_IDX = 0;
    private static final Integer SELECT_IDX = 1;
    private static final Integer NOW_IDX = 2;

    private final ShopService shopService;
    private final PaymentService paymentService;
    private final StaticsMapper staticsMapper;

    // 매장 조회
    private Shop getShop(Long shopId) {
        return shopService.findById(shopId);
    }

    // 상승,하락 비율 계산
    private Float calculateRatio(Long base, Long compare) {
        if (base == null || base == 0) {
            if (compare == null || compare == 0) {
                return 0F; // 기준값, 비교값 모두 0 = 변동 없음
            }
            return 100F; // 기준값 0, 비교값 0 X = 100% 상승
        }
        if (compare == null || compare == 0) {
            return -100F; // 비교값 0 = 100% 하락
        }
        float ratio = ((compare - base) / (float) base) * 100;
        return Math.round(ratio * 10) / 10F;
    }

    // 최근 5달 매출 조회
    public FiveMonthIncomeResponse getFiveMonthIncomes(String token) {
        Long shopId = shopService.getShopIdByToken(token);
        Shop shop = getShop(shopId);
        // 최근 5달 조회
        List<IncomeResponse> monthIncomeResponses = paymentService.getFiveMonthIncomes(shopId);
        return staticsMapper.toFiveMonthIncomeResponse(shop, monthIncomeResponses);
    }

    // 원하는 달 매출 조회
    public SelectMonthIncomeResponse getSelectMonthIncomes(String token, String month) {
        Long shopId = shopService.getShopIdByToken(token);
        Shop shop = getShop(shopId);
        // 원하는 달, 그 전달, 이번달 매출액 조회
        List<IncomeResponse> monthIncomeResponses = paymentService.getIncomeByMonth(shopId, month);
        Long previous = monthIncomeResponses.get(PREVIOUS_IDX).getIncome(); // 이전 달
        Long selected = monthIncomeResponses.get(SELECT_IDX).getIncome(); // 선택한 달
        Long current = monthIncomeResponses.get(NOW_IDX).getIncome(); // 이번 달
        // 원하는 이전달 기준 상승/하락 비율
        Float beforeRatio = calculateRatio(selected, previous);
        // 이번달 기준 상승/하락 비율
        Float nowRatio = calculateRatio(selected, current);
        return staticsMapper.toSelectMonthIncomeResponse(
                shop, monthIncomeResponses, beforeRatio, nowRatio);
    }

    // 최근 7일 매출 조회
    public WeekIncomeResponse getWeekIncomes(String token) {
        Long shopId = shopService.getShopIdByToken(token);
        Shop shop = getShop(shopId);
        // 최근 7일 조회
        List<IncomeResponse> monthIncomeResponses = paymentService.getWeekIncomes(shopId);
        return staticsMapper.toWeekIncomeResponse(shop, monthIncomeResponses);
    }
}
