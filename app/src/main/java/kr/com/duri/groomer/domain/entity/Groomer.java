package kr.com.duri.groomer.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import kr.com.duri.common.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "groomer")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Groomer extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "groomer_id")
    private Long id; // 미용사 ID

    @NotBlank
    @ManyToOne
    @JoinColumn(name = "shop_id")
    private Shop shop; // 매장 ID (FK)

    @NotBlank
    @Column(name = "groomer_email", unique = true)
    private String email; // 미용사 이메일

    @NotBlank
    @Column(name = "groomer_phone", unique = true)
    private String phone; // 미용사 전화번호

    @NotBlank
    @Column(name = "groomer_name")
    private String name; // 미용사 이름

    @NotBlank
    @Column(name = "groomer_history")
    private Integer history; // 미용사 경력 (년수)

    @Column(name = "groomer_image")
    private String image; // 프로필 이미지

    @Column(name = "groomer_info", columnDefinition = "TEXT")
    private String info; // 미용사 자기소개
}
