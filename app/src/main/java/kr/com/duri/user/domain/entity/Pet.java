package kr.com.duri.user.domain.entity;

import java.util.Date;

import jakarta.persistence.*;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private SiteUser user; // 고객 ID (FK, 1:N 관계)

    @Column(name = "pet_name")
    private String name; // 펫 이름

    @Column(name = "pet_breed")
    private String breed; // 펫 견종

    @Column(name = "pet_age")
    private Integer age; // 펫 나이

    @Column(name = "pet_weight")
    private Float weight; // 펫 몸무게

    @Column(name = "pet_gender")
    @Enumerated(EnumType.STRING)
    private Gender gender; // 펫 성별

    @Column(name = "pet_neutering")
    private Boolean neutering; // 중성화 여부(T중성화, F중성화안함)

    @Column(name = "pet_character")
    private String character; // 성격

    @Column(name = "pet_diseases")
    private String diseases; // 질환 정보

    @Column(name = "pet_image_url")
    private String image; // 펫 이미지 URL

    @Column(name = "last_grooming")
    private Date lastGrooming; // 마지막 미용 일자

    @Column(name = "deleted")
    private Boolean deleted; // 삭제 여부

    public static Pet createNewPet(
            SiteUser user,
            String name,
            String breed,
            Integer age,
            Float weight,
            String gender,
            Boolean neutering,
            String character,
            String diseases) {
        return Pet.builder()
                .user(user)
                .name(name)
                .breed(breed)
                .age(age)
                .weight(weight)
                .gender(Gender.valueOf(gender))
                .neutering(neutering)
                .character(character)
                .diseases(diseases)
                .deleted(false)
                .build();
    }

    // 시술 여부 수정 메서드
    public void updateLastGromming(Date lastDate) {
        this.lastGrooming = lastDate;
    }

    public Pet deletePet() {
        this.deleted = true;
        return this;
    }

    public Pet updatePet(
            String name,
            String breed,
            Integer age,
            Float weight,
            String gender,
            Boolean neutering,
            String character,
            String diseases,
            String imageUrl) {
        this.name = name;
        this.breed = breed;
        this.age = age;
        this.weight = weight;
        this.gender = Gender.valueOf(gender);
        this.neutering = neutering;
        this.character = character;
        this.diseases = diseases;
        if (imageUrl != null) {
            this.image = imageUrl;
        }
        return this;
    }
}
