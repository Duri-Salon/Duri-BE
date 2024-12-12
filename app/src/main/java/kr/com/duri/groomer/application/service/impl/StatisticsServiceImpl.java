package kr.com.duri.groomer.application.service.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import kr.com.duri.groomer.application.dto.response.StatisticsResponse;
import kr.com.duri.groomer.application.mapper.StatisticsMapper;
import kr.com.duri.groomer.application.service.StatisticsService;
import kr.com.duri.groomer.repository.QuotationRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {

    private final QuotationRepository quotationRepository;
    private final StatisticsMapper statisticsMapper;

    // 비율 계산
    private Float calculateRatio(Long count, Long totalCnt) {
        return (totalCnt > 0) ? Math.round((count / (float) totalCnt) * 1000) / 10F : 0F;
    }

    // Age Object to DTO (Mapper)
    private List<StatisticsResponse> calculateAgeStatistics(List<Object[]> data, Long totalCnt) {
        return data.stream()
                .map(
                        group -> {
                            String groupLabel = (String) group[0];
                            Long count = (Long) group[1];
                            Float ratio = calculateRatio(count, totalCnt);
                            return statisticsMapper.toStatisticsResponse(groupLabel, count, ratio);
                        })
                .toList();
    }

    // Disease, Character Object to DTO (Mapper)
    private List<StatisticsResponse> calculateEtcStatistics(
            List<Map<String, Object>> data, Long totalCnt) {
        return data.stream()
                .flatMap(map -> map.entrySet().stream())
                .map(
                        entry -> {
                            String label = entry.getKey();
                            Long count =
                                    entry.getValue() == null
                                            ? 0L
                                            : ((Number) entry.getValue()).longValue();
                            Float ratio = calculateRatio(count, totalCnt);
                            return statisticsMapper.toStatisticsResponse(label, count, ratio);
                        })
                .toList();
    }

    // 나이별 통계
    @Override
    public List<StatisticsResponse> getAgeStatistics(Long shopId) {
        List<Object[]> ageList = quotationRepository.getPetAgeStatistics(shopId);
        Long totalCnt = ageList.stream().mapToLong(group -> (Long) group[1]).sum();
        return calculateAgeStatistics(ageList, totalCnt);
    }

    // 질환별 통계
    @Override
    public List<StatisticsResponse> getDiseaseStatistics(Long shopId) {
        List<Map<String, Object>> diseaseList =
                quotationRepository.getPetDiseaseCharacterStatistics(shopId);
        Long totalCnt =
                diseaseList.stream()
                        .flatMap(map -> map.values().stream())
                        .mapToLong(value -> value == null ? 0L : ((Number) value).longValue())
                        .sum();
        return calculateEtcStatistics(diseaseList, totalCnt);
    }

    // 성격별 통계
    @Override
    public List<StatisticsResponse> getCharacterStatistics(Long shopId) {
        List<Map<String, Object>> characterList =
                quotationRepository.getPetCharacterStatistics(shopId);
        Long totalCnt =
                characterList.stream()
                        .flatMap(map -> map.values().stream())
                        .mapToLong(value -> value == null ? 0L : ((Number) value).longValue())
                        .sum();
        return calculateEtcStatistics(characterList, totalCnt);
    }

    // 가장 큰 개수의 통계 값 찾기
    private StatisticsResponse getBestStatistic(List<StatisticsResponse> statistics) {
        return statistics.stream()
                .max(Comparator.comparingLong(StatisticsResponse::getCount))
                .orElse(null);
    }

    // 키워드별 통계
    @Override
    public List<StatisticsResponse> getBestStatistics(Long shopId) {
        List<StatisticsResponse> bestStatistics = new ArrayList<>();
        // 나이대 통계
        bestStatistics.add(getBestStatistic(getAgeStatistics(shopId)));
        // 성격별 통계
        bestStatistics.add(getBestStatistic(getCharacterStatistics(shopId)));
        // 질환별 통계
        bestStatistics.add(getBestStatistic(getDiseaseStatistics(shopId)));
        return bestStatistics.stream().filter(stat -> stat != null).toList();
    }
}
