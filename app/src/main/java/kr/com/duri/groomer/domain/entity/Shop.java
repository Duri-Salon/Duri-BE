package kr.com.duri.groomer.domain.entity;

import java.time.LocalTime;

import jakarta.persistence.*;
import kr.com.duri.common.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "shop")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Shop extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shop_id")
    private Long id; // 매장 ID

    @Column(name = "shop_social_id", unique = true)
    private String socialId; // 소셜 제공 고유 아이디

    @Column(name = "shop_name")
    private String name; // 매장명

    @Column(name = "shop_info", columnDefinition = "TEXT")
    private String info; // 매장 소개

    @Column(name = "shop_email")
    private String email; // 이메일

    @Column(name = "shop_address")
    private String address; // 주소

    @Column(name = "shop_lat")
    private Double lat; // 매장위치 위도

    @Column(name = "shop_lon")
    private Double lon; // 매장위치 경도

    @Column(name = "shop_phone", unique = true)
    private String phone; // 매장 전화번호

    @Column(name = "shop_adtop")
    private Boolean adtop; // 광고 상단 노출 여부

    @Column(name = "shop_entry")
    private String entry; // 입점 승인 여부

    @Column(name = "shop_open_time")
    private LocalTime openTime; // 오픈시간

    @Column(name = "shop_close_time")
    private LocalTime closeTime; // 마감시간

    @Column(name = "shop_kakao_talk")
    private String kakaoTalk; // 오픈카톡방 링크

    @Column(name = "shop_rating")
    private Float rating; // 매장 별점

    @Builder.Default
    @Column(name = "new_shop")
    private Boolean newShop = true; // 신규 유저 여부

    @Builder.Default
    @Column(name = "shop_provider")
    private String provider = "Naver"; // 제공자

    @Column(name = "shop_business_registration_number")
    private String businessRegistrationNumber; // 사업자 등록번호

    @Column(name = "shop_groomer_license_number")
    private String groomerLicenseNumber; // 미용사 면허번호

    public static Shop createNewShop(String socialId, String email) {
        if (socialId == null || socialId.isEmpty()) {
            throw new IllegalArgumentException("Social ID must not be null or empty");
        }

        return Shop.builder().socialId(socialId).email(email).entry("W").build();
    }

    public Shop updateDetailWithOnboarding(
            String name,
            String phone,
            String address,
            Double lat,
            Double lon,
            String businessRegistrationNumber,
            String groomerLicenseNumber) {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.lat = lat;
        this.lon = lon;
        this.businessRegistrationNumber = businessRegistrationNumber;
        this.groomerLicenseNumber = groomerLicenseNumber;
        this.newShop = false;
        this.adtop = false;
        this.entry = "W";
        this.rating = 0.0f;
        return this;
    }

    public void updateRating(Float rating) {
        this.rating = rating;
    }
}
