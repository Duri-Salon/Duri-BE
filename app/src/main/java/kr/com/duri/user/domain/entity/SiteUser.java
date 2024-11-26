package kr.com.duri.user.domain.entity;

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
@Table(name = "user")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SiteUser extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id; // 고객 ID

    @NotBlank
    @Column(name = "user_social_id", unique = true)
    private String socialId; // 소셜 제공 고유 아이디

    @NotBlank
    @Column(name = "user_email", unique = true)
    private String email; // 이메일

    @NotBlank
    @Column(name = "user_name")
    private String name; // 유저 이름

    @NotBlank
    @Column(name = "user_phone", unique = true)
    private String phone; // 전화번호

    @Column(name = "user_gender")
    @Enumerated(EnumType.STRING)
    private Gender gender; // 성별

    @Column(name = "user_image_url")
    private String image; // 프로필 이미지

    @NotBlank
    @Column(name = "user_birth")
    private String birth; // 생년월일

    @Builder.Default
    @Column(name = "user_provider")
    private String provider = "Naver"; // 가입경로(소셜 종류)

    @Builder.Default
    @Column(name = "user_active")
    private Boolean active = true; // 휴면 여부(T활성화, F비활성화)

    @Builder.Default
    @Column(name = "user_stamp", columnDefinition = "INT DEFAULT 0")
    private Integer stamp = 0; // 도장 개수(기본값 0)
}
