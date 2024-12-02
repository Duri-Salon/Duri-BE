package kr.com.duri.user.domain.entity;

import java.time.LocalDate;

import jakarta.persistence.*;
import kr.com.duri.common.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "quotation_req") // 테이블 이름 변경
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuotationReq extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quotation_req_id") // 컬럼 이름 변경
    private Long id; // 요청서 ID

    @ManyToOne
    @JoinColumn(name = "pet_id")
    private Pet pet; // 반려견 ID (FK)

    @Column(name = "quotation_req_close") // 컬럼 이름 변경
    private Boolean close; // 마감 여부 (T: 마감, F: 진행중)

    @Column(name = "quotation_req_max_price") // 컬럼 이름 변경
    private Integer maxPrice; // 가능한 최대 금액

    @Column(name = "quotation_req_menu") // 컬럼 이름 변경
    private String menu; // 미용 메뉴 (가위컷, 부분_목욕, 스포팅 등)

    @Column(name = "quotation_req_add_menu") // 컬럼 이름 변경
    private String addMenu; // 추가 미용 메뉴

    @Column(name = "quotation_req_special_menu") // 컬럼 이름 변경
    private String specialMenu; // 스페셜케어

    @Column(name = "quotation_req_design") // 컬럼 이름 변경
    private String design; // 디자인컷

    @Column(name = "pet_size")
    private String petSize; // 반려견 품종(소형견/중형견/대형견)

    @Column(name = "quotation_req_etc", columnDefinition = "TEXT") // 컬럼 이름 변경
    private String etc; // 기타 요구사항

    @Column(name = "quotation_req_day") // 컬럼 이름 변경
    private LocalDate day; // 예약일 (YYYY-MM-DD)

    @Column(name = "quotation_req_time9") // 컬럼 이름 변경
    private Boolean time9; // 9시 희망 여부 (T: 희망, F: 비희망)

    @Column(name = "quotation_req_time10") // 컬럼 이름 변경
    private Boolean time10; // 10시 희망 여부 (T: 희망, F: 비희망)

    @Column(name = "quotation_req_time11") // 컬럼 이름 변경
    private Boolean time11; // 11시 희망 여부 (T: 희망, F: 비희망)

    @Column(name = "quotation_req_time12") // 컬럼 이름 변경
    private Boolean time12; // 12시 희망 여부 (T: 희망, F: 비희망)

    @Column(name = "quotation_req_time13") // 컬럼 이름 변경
    private Boolean time13; // 13시 희망 여부 (T: 희망, F: 비희망)

    @Column(name = "quotation_req_time14") // 컬럼 이름 변경
    private Boolean time14; // 14시 희망 여부 (T: 희망, F: 비희망)

    @Column(name = "quotation_req_time15") // 컬럼 이름 변경
    private Boolean time15; // 15시 희망 여부 (T: 희망, F: 비희망)

    @Column(name = "quotation_req_time16") // 컬럼 이름 변경
    private Boolean time16; // 16시 희망 여부 (T: 희망, F: 비희망)

    @Column(name = "quotation_req_time17") // 컬럼 이름 변경
    private Boolean time17; // 17시 희망 여부 (T: 희망, F: 비희망)

    @Column(name = "quotation_req_time18") // 컬럼 이름 변경
    private Boolean time18; // 18시 희망 여부 (T: 희망, F: 비희망)
}
