package kr.com.duri.groomer.domain.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
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

    @OneToOne
    @JoinColumn(name = "request_id")
    private Request request; // 견적 요청 ID (FK)

    @Column(name = "quotation_price")
    private String price; // 견적 금액

    @Column(name = "quotation_memo", columnDefinition = "TEXT")
    private String memo; // 메모

    @Column(name = "quotation_status")
    @Enumerated(EnumType.STRING)
    private QuotationStatus status; // 상태 (대기(W), 승인(A), 만료(E))

    @Column(name = "quotation_start_datetime")
    private LocalDateTime startDateTime; // 미용 시작 시간 (YYYY-MM-DD HH:MM)

    @Column(name = "quotation_end_datetime")
    private LocalDateTime endDateTime; // 미용 종료 시간 (YYYY-MM-DD HH:MM)

    @Column(name = "complete")
    private Boolean complete; // 미용 시술 여부 (True(완료), False(미완료))

    @Column(name = "noshow")
    @Builder.Default
    private Boolean noShow = false; // 노쇼 여부 (True(노쇼), False(노쇼 X))

    // 견적서 수정 메서드
    public Quotation update(
            String priceJson, String memo, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        this.price = priceJson;
        this.memo = memo;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        return this;
    }

    // 시술 여부 수정 메서드
    public void updateComplete(Boolean complete) {
        this.complete = complete;
    }

    // 노쇼 여부 수정 메서드
    public void updateNoShow(Boolean noShow) {
        this.noShow = noShow;
    }

    // 상태 변경 메서드
    public void updateStatus(QuotationStatus status) {
        this.status = status;
    }
}
