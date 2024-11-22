package kr.com.duri.user.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import kr.com.duri.common.entity.BaseEntity;
import kr.com.duri.groomer.domain.entity.Shop;
import kr.com.duri.user.domain.Enum.QuotationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "request")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Request extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
    private Long id; // 견적서 ID

    @NotBlank
    @ManyToOne
    @JoinColumn(name = "quotation_id")
    private QuotationReq quotation; // 견적요청서 ID (FK)

    @NotBlank
    @ManyToOne
    @JoinColumn(name = "shop_id")
    private Shop shop; // 매장 ID (FK)

    @NotBlank
    @Column(name = "request_status")
    @Enumerated(EnumType.STRING)
    private QuotationStatus status; // 상태 (대기(W), 승인(A), 만료(E))
}
