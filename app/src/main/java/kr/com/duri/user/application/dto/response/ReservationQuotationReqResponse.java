package kr.com.duri.user.application.dto.response;

import kr.com.duri.user.domain.entity.Pet;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservationQuotationReqResponse {
    private Long requestId; // 요청 ID
    private Long userId; // 고객 ID
    private Long petId; // 애완견 ID
    private PetDetailResponse petDetailResponse; //펫 정보들
    private String groomerName; // 미용사 이름
    private String groomerImage; // 미용사 사진
    private Integer totalPrice; // 최종 결제 금액 (totalPrice만 추가)
    private Integer dday; // d-day 며칠
    private LocalDate date; // 미용 일자
    private Time startTime; // 시작 시간
    private Time endTime; // 종료 시간
}
