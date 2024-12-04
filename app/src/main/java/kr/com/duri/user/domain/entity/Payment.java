package kr.com.duri.user.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import kr.com.duri.common.entity.BaseEntity;
import kr.com.duri.groomer.domain.entity.Quotation;
import kr.com.duri.user.domain.Enum.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "payment")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long id; // 결제 ID

    @NotBlank
    @OneToOne
    @JoinColumn(name = "quotation_id")
    private Quotation quotation; // 견적서 ID (FK)

    @NotBlank
    @Column(name = "toss_key")
    private String tossKey; // 토스에서 제공하는 결제 키 값

    @Column(name = "original_price")
    private Integer originalPrice; // 결제 금액

    @Column(name = "discount_price")
    private Integer discountPrice = 0; // 할인 금액 (기본값 0)

    @Column(name = "price")
    private Integer price; // 최종 결제 금액

    @NotBlank
    @Column(name = "payment_status")
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
}
