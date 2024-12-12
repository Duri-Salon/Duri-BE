package kr.com.duri.groomer.controller;

import kr.com.duri.common.response.CommonResponseEntity;
import kr.com.duri.groomer.application.dto.response.FiveMonthIncomeResponse;
import kr.com.duri.groomer.application.dto.response.SelectMonthIncomeResponse;
import kr.com.duri.groomer.application.dto.response.WeekIncomeResponse;
import kr.com.duri.groomer.application.facade.StaticsFacade;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/statistics")
public class StaticsController {

    private final StaticsFacade staticsFacade;

    // DURI-291 : 최근 5달 매출 조회
    @GetMapping("/income/five-month")
    public CommonResponseEntity<FiveMonthIncomeResponse> getFiveMonthIncomes(
            @RequestHeader("authorization_shop") String token) {
        return CommonResponseEntity.success(staticsFacade.getFiveMonthIncomes(token));
    }

    // DURI-292 : 원하는 달 매출 조회
    @GetMapping("/income/month")
    public CommonResponseEntity<SelectMonthIncomeResponse> getMonthIncomes(
            @RequestHeader("authorization_shop") String token,
            @RequestParam("month") String month) {
        return CommonResponseEntity.success(staticsFacade.getSelectMonthIncomes(token, month));
    }

    // DURI-348 : 최근 7일 매출 조회
    @GetMapping("/income/week")
    public CommonResponseEntity<WeekIncomeResponse> getMonthIncomes(
            @RequestHeader("authorization_shop") String token) {
        return CommonResponseEntity.success(staticsFacade.getWeekIncomes(token));
    }

    // DURI-349 : 반려견 나이별 누적 조회
    // @GetMapping("/age")

    // DURI-350 : 반려견 질환별 누적 조회
    // @GetMapping("/disease")

    // DURI-351 : 반려견 성격별 누적 조회
    // @GetMapping("/character")

    // DURI-352 : 매장 베스트 키워드 조회
    // @GetMapping("/keyword")
}
