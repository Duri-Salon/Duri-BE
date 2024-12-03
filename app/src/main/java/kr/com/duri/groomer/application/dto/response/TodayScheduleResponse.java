package kr.com.duri.groomer.application.dto.response;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TodayScheduleResponse {
    private String date =
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")); // 오늘 날짜
    private String startTime; // 미용 시작 시간 (YYYY-MM-DD HH:MM)
    private Long petId; // 펫 ID
    private String petName; // 펫 이름
    private String breed; // 견종
    private String gender; // 성별
    private Integer weight; // 무게
    private String groomerName; // 미용사 이름
}
