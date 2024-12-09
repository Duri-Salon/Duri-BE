package kr.com.duri.groomer.domain.entity;

import jakarta.persistence.*;
import kr.com.duri.common.entity.BaseEntity;
import kr.com.duri.groomer.domain.Enum.Gender;
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

    @ManyToOne
    @JoinColumn(name = "shop_id")
    private Shop shop; // 매장 ID (FK)

    @Column(name = "groomer_email", unique = true)
    private String email; // 미용사 이메일

    @Column(name = "groomer_phone", unique = true)
    private String phone; // 미용사 전화번호

    @Column(name = "groomer_name")
    private String name; // 미용사 이름

    @Column(name = "groomer_age")
    private Integer age; // 미용사 나이

    @Enumerated(EnumType.STRING)
    @Column(name = "groomer_gender")
    private Gender gender; // 미용사 성별

    @Column(name = "groomer_history")
    private Integer history; // 미용사 경력 (월수)

    @Column(name = "groomer_image")
    private String image; // 프로필 이미지

    @Column(name = "groomer_info", columnDefinition = "TEXT")
    private String info; // 미용사 자기소개

    @Column(name = "groomer_license")
    private String license; // 자격증

    public static Groomer createNewGroomerWithOnboarding(
            Shop shop,
            String name,
            Integer age,
            String gender,
            Integer history,
            String profileImage,
            String license) {
        return Groomer.builder()
                .shop(shop)
                .name(name)
                .age(age)
                .gender(Gender.valueOf(gender))
                .history(history)
                .image(profileImage)
                .license(license)
                .build();
    }

    public static Groomer createNewGroomer(Shop shop, String name, Integer age, String gender, String email, String phone, Integer history, String image, String info, String license) {
        return Groomer.builder()
                .shop(shop)
                .name(name)
                .age(age)
                .gender(Gender.valueOf(gender))
                .email(email)
                .phone(phone)
                .history(history)
                .image(image)
                .info(info)
                .license(license)
                .build();
    }
}
