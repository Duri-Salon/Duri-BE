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
@Table(name = "certification")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Certification extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "certification_id")
    private Long id; // 자격증 ID

    @NotBlank
    @ManyToOne
    @JoinColumn(name = "groomer_id", nullable = false)
    private Groomer groomer; // 미용사 ID (FK)

    @NotBlank
    @Column(name = "certification_name")
    private String name; // 자격증 이름
}
