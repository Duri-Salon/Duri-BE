package kr.com.duri.groomer.repository;

import java.util.List;
import java.util.Optional;

import kr.com.duri.groomer.domain.Enum.EntryStatus;
import kr.com.duri.groomer.domain.entity.Shop;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ShopRepository extends JpaRepository<Shop, Long> {
    Optional<Shop> findBySocialId(String socialId);

    // shopId로 존재 여부를 확인하는 메서드
    boolean existsById(Long shopId);

    // 주변 매장 리스트 조회
    @Query(
            value =
                    """
        SELECT s.shop_id, s.shop_name, s.shop_address, s.shop_lat, s.shop_lon, s.shop_phone, s.shop_open_time, s.shop_close_time, s.shop_rating,
               (6371000 * ACOS(COS(RADIANS(:lat)) * COS(RADIANS(s.shop_lat)) * COS(RADIANS(s.shop_lon) - RADIANS(:lon)) + SIN(RADIANS(:lat)) * SIN(RADIANS(s.shop_lat)))) AS d
        FROM shop s
        WHERE (6371000 * ACOS(COS(RADIANS(:lat)) * COS(RADIANS(s.shop_lat)) * COS(RADIANS(s.shop_lon) - RADIANS(:lon)) + SIN(RADIANS(:lat)) * SIN(RADIANS(s.shop_lat)))) <= :radius
    """,
            nativeQuery = true)
    List<Object[]> findShopsWithinRadius(
            @Param("lat") Double lat, @Param("lon") Double lon, @Param("radius") Double radius);

    // 주변 매장 리스트 조회 (매장)
    @Query(
            value =
                    """
                SELECT * FROM shop s
                ORDER BY (6371000 * ACOS(COS(RADIANS(:lat)) * COS(RADIANS(s.shop_lat)) * COS(RADIANS(s.shop_lon) - RADIANS(:lon)) + SIN(RADIANS(:lat)) * SIN(RADIANS(s.shop_lat)))) ASC
            """,
            nativeQuery = true)
    List<Shop> findShopswithSortAsc(@Param("lat") Double lat, @Param("lon") Double lon);

    // 주변 매장 검색
    @Query(
            value =
                    """
        SELECT s.shop_id, s.shop_name, s.shop_address, s.shop_lat, s.shop_lon, s.shop_phone, s.shop_open_time, s.shop_close_time, s.shop_rating,
               (6371000 * ACOS(COS(RADIANS(:lat)) * COS(RADIANS(s.shop_lat)) * COS(RADIANS(s.shop_lon) - RADIANS(:lon)) + SIN(RADIANS(:lat)) * SIN(RADIANS(s.shop_lat)))) AS d
        FROM shop s
        WHERE s.shop_name LIKE CONCAT('%', :search, '%') or s.shop_address LIKE CONCAT('%', :search, '%')
    """,
            nativeQuery = true)
    List<Object[]> findShopsWithSearch(
            @Param("search") String search, @Param("lat") Double lat, @Param("lon") Double lon);

    // 반경 내 매장 리스트 조회
    @Query(
            value =
                    """
            SELECT s
            FROM Shop s
            WHERE (6371000 * ACOS(
                COS(RADIANS(:lat)) * COS(RADIANS(s.lat)) * COS(RADIANS(s.lon) - RADIANS(:lon)) +
                SIN(RADIANS(:lat)) * SIN(RADIANS(s.lat))
            )) <= :radians
        """)
    List<Shop> findShopsByRadius(
            @Param("lat") Double lat, @Param("lon") Double lon, @Param("radians") Double radians);

    List<Shop> findByEntryAndNewShopFalse(EntryStatus entry);
}
