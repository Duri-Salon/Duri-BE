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
}
