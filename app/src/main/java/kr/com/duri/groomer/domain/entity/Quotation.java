package kr.com.duri.groomer.domain.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import kr.com.duri.common.entity.BaseEntity;
import kr.com.duri.user.domain.Enum.QuotationStatus;
import kr.com.duri.user.domain.entity.Request;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "quotation")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Quotation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quotation_id")
    private Long id; // 견적서 ID

    @NotBlank
    @OneToOne
    @JoinColumn(name = "request_id")
    private Request request; // 견적 요청 ID (FK)

    @Column(name = "quotation_price")
    private Integer price; // 견적 금액

    @Column(name = "quotation_add_menu")
    private String addMenu; // 추가 미용 메뉴

    @Column(name = "quotation_special_menu")
    private String specialMenu; // 스페셜케어

    @Column(name = "quotation_memo", columnDefinition = "TEXT")
    private String memo; // 메모

    @NotBlank
    @Column(name = "quotation_status")
    @Enumerated(EnumType.STRING)
    private QuotationStatus status; // 상태 (대기(W), 승인(A), 만료(E))

    @NotBlank
    @Column(name = "quotation_start_datetime")
    private LocalDateTime startDateTime; // 미용 시작 시간 (YYYY-MM-DD HH:MM)

    @NotBlank
    @Column(name = "quotation_end_datetime")
    private LocalDateTime endDateTime; // 미용 종료 시간 (YYYY-MM-DD HH:MM)
}
