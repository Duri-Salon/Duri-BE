package kr.com.duri.groomer.application.service;

import java.util.List;

import kr.com.duri.groomer.application.dto.response.StatisticsResponse;

public interface StatisticsService {

    // 나이별 통계
    List<StatisticsResponse> getAgeStatistics(Long shopId);

    // 질환별 통계
    List<StatisticsResponse> getDiseaseStatistics(Long shopId);

    // 성격별 통계
    List<StatisticsResponse> getCharacterStatistics(Long shopId);

    // 키워드별 통계
    List<StatisticsResponse> getBestStatistics(Long shopId);
}
