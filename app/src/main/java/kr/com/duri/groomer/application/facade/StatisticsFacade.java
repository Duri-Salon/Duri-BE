package kr.com.duri.groomer.application.facade;

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
import kr.com.duri.groomer.application.mapper.StatisticsMapper;
import kr.com.duri.groomer.application.service.QuotationService;
import kr.com.duri.groomer.application.service.ShopService;
import kr.com.duri.groomer.application.service.StatisticsService;
import kr.com.duri.groomer.domain.entity.Shop;
import kr.com.duri.user.application.service.PaymentService;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StatisticsFacade {

    private static final Integer PREVIOUS_IDX = 0;
    private static final Integer SELECT_IDX = 1;
    private static final Integer NOW_IDX = 2;

    private final ShopService shopService;
    private final QuotationService quotationService;
    private final PaymentService paymentService;
    private final StatisticsMapper statisticsMapper;
    private final StatisticsService statisticsService;

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
        // 이번달 기준 이전달 상승/하락
        Long nowMonthIncome = monthIncomeResponses.get(monthIncomeResponses.size() - 1).getIncome();
        Long beforeMonthIncome =
            monthIncomeResponses.get(monthIncomeResponses.size() - 2).getIncome();
        Float beforeRatio = calculateRatio(beforeMonthIncome, nowMonthIncome);
        return statisticsMapper.toFiveMonthIncomeResponse(shop, monthIncomeResponses, beforeRatio);
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
        return statisticsMapper.toSelectMonthIncomeResponse(
                shop, monthIncomeResponses, beforeRatio, nowRatio);
    }

    // 최근 7일 매출 조회
    public WeekIncomeResponse getWeekIncomes(String token) {
        Long shopId = shopService.getShopIdByToken(token);
        Shop shop = getShop(shopId);
        // 최근 7일 조회
        List<IncomeResponse> monthIncomeResponses = paymentService.getWeekIncomes(shopId);
        return statisticsMapper.toWeekIncomeResponse(shop, monthIncomeResponses);
    }

    // 반려견 나이별 누적 조회
    public AgeStatisticsResponse getAgeStatistics(String token) {
        Long shopId = shopService.getShopIdByToken(token);
        Shop shop = getShop(shopId);
        // 나이별 조회
        List<StatisticsResponse> ageResponses = statisticsService.getAgeStatistics(shopId);
        return statisticsMapper.toAgeStatisticsResponse(shop, ageResponses);
    }

    // 반려견 질환별 누적 조회
    public DiseaseStatisticsResponse getDiseaseStatistics(String token) {
        Long shopId = shopService.getShopIdByToken(token);
        Shop shop = getShop(shopId);
        // 질환별 조회
        List<StatisticsResponse> diseaseResponses = statisticsService.getDiseaseStatistics(shopId);
        return statisticsMapper.toDiseaseStatisticsResponse(shop, diseaseResponses);
    }

    // 반려견 성격별 누적 조회
    public CharacterStatisticsResponse getCharacterStatistics(String token) {
        Long shopId = shopService.getShopIdByToken(token);
        Shop shop = getShop(shopId);
        // 성격별 조회
        List<StatisticsResponse> characterResponses =
                statisticsService.getCharacterStatistics(shopId);
        return statisticsMapper.toCharacterStatisticsResponse(shop, characterResponses);
    }

    // 매장 최고 키워드별 조회
    public BestStatisticsResponse getBestStatistics(String token) {
        Long shopId = shopService.getShopIdByToken(token);
        Shop shop = getShop(shopId);
        // 성격별 조회
        List<StatisticsResponse> bestResponses = statisticsService.getBestStatistics(shopId);
        return statisticsMapper.toBestStatisticsResponse(shop, bestResponses);
    }
}
