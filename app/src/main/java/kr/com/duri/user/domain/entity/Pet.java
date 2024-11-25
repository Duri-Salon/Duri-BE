package kr.com.duri.user.domain.entity;

import java.util.Date;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import kr.com.duri.common.entity.BaseEntity;
import kr.com.duri.user.domain.Enum.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "pet")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pet extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pet_id")
    private Long id; // 반려견 ID

    @NotBlank
    @ManyToOne
    @JoinColumn(name = "user_id")
    private SiteUser user; // 고객 ID (FK, 1:N 관계)

    @NotBlank
    @Column(name = "pet_name")
    private String name; // 펫 이름

    @NotBlank
    @Column(name = "pet_breed")
    private String breed; // 펫 견종

    @NotBlank
    @Column(name = "pet_age")
    private Integer age; // 펫 나이

    @NotBlank
    @Column(name = "pet_weight")
    private Integer weight; // 펫 몸무게

    @NotBlank
    @Column(name = "pet_gender")
    @Enumerated(EnumType.STRING)
    private Gender gender; // 펫 성별

    @NotBlank
    @Column(name = "pet_neutering")
    private Boolean neutering; // 중성화 여부(T중성화, F중성화안함)

    @NotBlank
    @Column(name = "pet_biting")
    private Boolean biting; // 입질 여부(T입질있음, F입질없음)

    @Column(name = "pet_memo", columnDefinition = "TEXT")
    private String memo; // 메모

    @Column(name = "pet_image_url")
    private String image; // 펫 이미지 URL

    @Column(name = "last_grooming")
    private Date lastGrooming; // 마지막 미용 일자
}
