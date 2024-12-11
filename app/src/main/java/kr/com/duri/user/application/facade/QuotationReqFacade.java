package kr.com.duri.user.application.facade;

import java.util.List;
import java.util.stream.Collectors;

import kr.com.duri.groomer.application.service.GroomerService;
import kr.com.duri.groomer.application.service.QuotationService;
import kr.com.duri.groomer.application.service.ShopService;
import kr.com.duri.groomer.domain.entity.Groomer;
import kr.com.duri.groomer.domain.entity.Quotation;
import kr.com.duri.groomer.domain.entity.Shop;
import kr.com.duri.groomer.exception.ShopNotFoundException;
import kr.com.duri.user.application.dto.request.NewQuotationReqRequest;
import kr.com.duri.user.application.dto.request.QuotationReqDetailRequest;
import kr.com.duri.user.application.dto.response.*;
import kr.com.duri.user.application.mapper.QuotationReqMapper;
import kr.com.duri.user.application.service.PetService;
import kr.com.duri.user.application.service.QuotationReqService;
import kr.com.duri.user.application.service.RequestService;
import kr.com.duri.user.domain.entity.Pet;
import kr.com.duri.user.domain.entity.QuotationReq;
import kr.com.duri.user.domain.entity.Request;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuotationReqFacade {

    private final QuotationReqService quotationReqService;
    private final QuotationService quotationService;
    private final RequestService requestService;
    private final GroomerService groomerService;
    private final ShopService shopService;
    private final PetService petService;
    private final QuotationReqMapper quotationReqMapper;

    // 새로운 견적 요청서 리스트(Groomer)
    public List<NewQuotationReqResponse> getNewRequests(Long shopId) {
        // 1. shopId 유효성 확인
        boolean shopExists = shopService.existsByShopId(shopId);
        if (!shopExists) {
            throw new ShopNotFoundException("해당하는 미용업체가 없습니다.");
        }

        // 2. WAITING 상태의 요청 조회
        List<Request> requests = requestService.getNewRequestsByShopId(shopId);

        // 3. 요청 목록을 응답 DTO로 변환
        return requests.stream()
                .map(quotationReqMapper::toNewQuotationResponse)
                .collect(Collectors.toList());
    }

    // 견적 요청 상세 정보(Groomer)
    public NewQuotationReqDetailResponse getQuotationReqDetail(Long requestId) {
        // 견적 요청 ID로 데이터를 조회
        Request request = requestService.getRequestById(requestId);
        Long shopId = request.getShop().getId();

        // shopId로 groomer찾기
        Groomer groomer = groomerService.getGroomerByShopId(shopId);

        // 3. Mapper를 사용하여 DTO 생성
        return quotationReqMapper.toQuotationReqDetailResponse(request, groomer);
    }

    // 답장한 견적 요청서 리스트(Groomer)
    public List<ApprovedQuotationReqResponse> getApprovedRequests(Long shopId) {
        // 1. shopId 유효성 확인
        boolean shopExists = shopService.existsByShopId(shopId);
        if (!shopExists) {
            throw new ShopNotFoundException("해당하는 미용업체가 없습니다.");
        }

        // 2. APPROVED 상태의 요청 조회
        List<Request> requests = requestService.getApprovedRequestsByShopId(shopId);

        // 3. 요청 목록을 응답 DTO로 변환
        return requests.stream()
                .map(
                        request -> {
                            Quotation quotation = quotationService.findByRequestId(request.getId());
                            return quotationReqMapper.toApprovedQuotationResponse(quotation);
                        })
                .collect(Collectors.toList());
    }

    // 예약 확정한 견적 요청서 리스트(Groomer)
    public List<ReservationQuotationReqResponse> getReservationRequests(Long shopId) {
        // 1. shopId 유효성 확인
        boolean shopExists = shopService.existsByShopId(shopId);
        if (!shopExists) {
            throw new ShopNotFoundException("해당하는 미용업체가 없습니다.");
        }
        // shopId로 groomer찾기
        Groomer groomer = groomerService.getGroomerByShopId(shopId);

        // 2. Quotation의 상태가 APPROVED이면서 Complete가 false인 요청 조회
        List<Request> requests = requestService.getReservationRequestsByShopId(shopId);

        // 3. 요청 목록을 응답 DTO로 변환
        return requests.stream()
                .map(
                        request -> {
                            Quotation quotation = quotationService.findByRequestId(request.getId());
                            return quotationReqMapper.toReservationQuotationResponse(
                                    quotation, groomer);
                        })
                .collect(Collectors.toList());
    }

    // 시술 완료한 견적 요청서 리스트(Groomer)
    public List<ReservationQuotationReqResponse> getCompleteRequests(Long shopId) {
        // 1. shopId 유효성 확인
        boolean shopExists = shopService.existsByShopId(shopId);
        if (!shopExists) {
            throw new ShopNotFoundException("해당하는 미용업체가 없습니다.");
        }
        // shopId로 groomer찾기
        Groomer groomer = groomerService.getGroomerByShopId(shopId);

        // 2. Quotation의 상태가 APPROVED이면서 Complete가 true인 요청 조회
        List<Request> requests = requestService.getCompleteRequestsByShopId(shopId);

        // 3. 요청 목록을 응답 DTO로 변환
        return requests.stream()
                .map(
                        request -> {
                            Quotation quotation = quotationService.findByRequestId(request.getId());
                            return quotationReqMapper.toReservationQuotationResponse(
                                    quotation, groomer);
                        })
                .collect(Collectors.toList());
    }

    // 견적 요청서 작성(User)
    public Long createQuotationRequest(NewQuotationReqRequest newQuotationReqRequest) {
        Pet pet = petService.findById(newQuotationReqRequest.getPetId());

        // 1. 견적요청서 DTO로 엔티티 생성
        QuotationReq quotationReq =
                quotationReqMapper.toQuotationReqEntity(newQuotationReqRequest, pet);

        // 2. 견적요청서 저장
        QuotationReq savedQuotationReq = quotationReqService.saveQuotationRequest(quotationReq);

        // 3. 요청 DTO로 엔티티 생성
        List<Request> requests =
                quotationReqMapper.toRequestEntities(
                        savedQuotationReq, newQuotationReqRequest.getShopIds());

        // 4. 요청 저장
        requests.forEach(
                request -> {
                    shopService.findById(request.getShop().getId());
                });
        requestService.saveRequests(requests);

        // 5. 견적 요청서 ID 출력
        return savedQuotationReq.getId();
    }

    // 견적 요청서 목록 조회
    public List<QuotationListResponse> getQuotationReqsByUserId(Long userId) {
        // 1. User의 Pet조회
        Pet pet = petService.findById(userId);

        // 2. Pet으로 QuotationReq 목록 조회
        List<QuotationReq> quotationReqs = quotationReqService.findByPetId(pet.getId());

        // 3. 각 QuotationReq에 대해 필요한 데이터 처리
        return quotationReqs.stream()
                .map(
                        quotationReq -> {
                            List<Request> requests =
                                    requestService.findByQuotationId(quotationReq.getId());
                            return quotationReqMapper.toQuotationReqListResponse(
                                    quotationReq, requests);
                        })
                .collect(Collectors.toList());
    }

    // 지난 견적 요청 상세 정보(User)
    public LastQuotationReqResponse getLastQuotationReqDetail(Long userId) {
        Pet pet = petService.findById(userId);
        QuotationReq quotationReq = quotationReqService.findLatestByPetId(pet.getId());

        return quotationReqMapper.toLastQuotationReqResponse(pet, quotationReq);
    }

    // 견적 요청서 및 견적서 순위 조회
    public QuotationReqDetailResponse getQuotationReqDetail(
            QuotationReqDetailRequest quotationReqDetailRequest) {
        // 1. QuotationReq
        QuotationReq quotationReq =
                quotationReqService.getQuotationReqById(
                        quotationReqDetailRequest.getQuotationReqId());

        // 2. Request
        List<Request> requests =
                requestService.findByQuotationId(quotationReqDetailRequest.getQuotationReqId());
        List<Long> requestsIds = requests.stream().map(Request::getId).collect(Collectors.toList());

        // 3. Quotation
        List<Quotation> quotations = quotationService.findByRequestIdsOrderByPrice(requestsIds);

        // 4. 순위 1등 매장 계산
        ShopBestResponse bestDistanceShop =
                calculateBestDistanceShop(
                        quotations,
                        quotationReqDetailRequest.getLat(),
                        quotationReqDetailRequest.getLon());
        ShopBestResponse bestPriceShop = calculateBestPriceShop(quotations);
        ShopBestResponse bestRatingShop = calculateBestRatingShop(quotations);
        ShopBestResponse bestShop =
                calculateBestShop(
                        quotations,
                        quotationReqDetailRequest.getLat(),
                        quotationReqDetailRequest.getLon());

        // 5. Request에 대한 Quotation 정보 매핑
        List<QuotationReqDetailRequestListResponse> requestQuotations =
                requests.stream()
                        .map(
                                request -> {
                                    Quotation quotation =
                                            quotations.stream()
                                                    .filter(
                                                            q ->
                                                                    q.getRequest()
                                                                            .getId()
                                                                            .equals(
                                                                                    request
                                                                                            .getId()))
                                                    .findFirst()
                                                    .orElse(null);
                                    return quotationReqMapper.toRequestListResponse(
                                            request, quotation);
                                })
                        .collect(Collectors.toList());

        // 6. 결과 반환 (Mapper 사용)
        return quotationReqMapper.toResponse(
                quotationReq,
                bestDistanceShop,
                bestPriceShop,
                bestRatingShop,
                bestShop,
                requestQuotations);
    }

    // 거리순 1등
    private ShopBestResponse calculateBestDistanceShop(
            List<Quotation> quotations, Double lat, Double lon) {
        ShopBestResponse bestDistanceShop = null;
        double minDistance = Double.MAX_VALUE;

        for (Quotation quotation : quotations) {
            Shop shop = quotation.getRequest().getShop();
            if (shop == null) {
                continue;
            }

            double shopLat = shop.getLat();
            double shopLon = shop.getLon();

            // 현재 매장과 요청된 위치(lat, lon) 간의 거리 계산
            double distance = calculateDistance(shopLat, shopLon, lat, lon);

            if (distance < minDistance) {
                minDistance = distance;
                bestDistanceShop =
                        new ShopBestResponse(shop.getName(), "shop_image_url"); // 나중에 이미지 수정 필요!!!
            }
        }

        return bestDistanceShop;
    }

    // 가격순 1등
    private ShopBestResponse calculateBestPriceShop(List<Quotation> quotations) {
        Quotation bestQuotation = null;
        Integer minPrice = Integer.MAX_VALUE;

        for (Quotation quotation : quotations) {
            Integer totalPrice = quotationReqMapper.extractTotalPriceFromJson(quotation.getPrice());

            if (totalPrice != null && totalPrice < minPrice) {
                minPrice = totalPrice; // 가장 저렴한 가격 갱신
                bestQuotation = quotation; // 가장 저렴한 Quotation 객체 저장
            }
        }

        if (bestQuotation != null) {
            String shopName =
                    bestQuotation.getRequest().getShop().getName(); // Quotation의 Request에서 매장 이름 추출
            String shopImage = "shop_image_url"; // 임시 이미지 URL (실제 구현 시 변경 필요)

            return new ShopBestResponse(shopName, shopImage);
        } else {
            return null;
        }
    }

    // 평점순 1등
    private ShopBestResponse calculateBestRatingShop(List<Quotation> quotations) {
        // 1. 가장 높은 평점을 가진 매장 계산
        Quotation bestQuotation = null;
        Float maxRating = (float) -1.0; // 최대 평점 초기화 (평점이 0 이상이므로 -1로 초기화)

        // 2. Quotation 리스트 순회하면서 가장 높은 평점 찾기
        for (Quotation quotation : quotations) {
            Float rating =
                    quotation
                            .getRequest()
                            .getShop()
                            .getRating(); // Quotation의 Request에서 연결된 Shop의 평점 가져오기

            if (rating != null && rating > maxRating) {
                maxRating = rating; // 가장 높은 평점 갱신
                bestQuotation = quotation; // 가장 높은 평점을 가진 Quotation 객체 저장
            }
        }

        // 3. 가장 높은 평점을 가진 매장 정보 반환
        if (bestQuotation != null) {
            String shopName =
                    bestQuotation.getRequest().getShop().getName(); // Quotation의 Request에서 매장 이름 추출
            String shopImage = "shop_image_url"; // 임시 이미지 URL (실제 구현 시 변경 필요)

            // 가장 높은 평점을 가진 매장 정보로 ShopBestResponse 객체 반환
            return new ShopBestResponse(shopName, shopImage);
        } else {
            // 매장이 없으면 null 반환
            return null;
        }
    }

    // 총합 1등
    private ShopBestResponse calculateBestShop(List<Quotation> quotations, Double lat, Double lon) {
        ShopBestResponse bestShop = null;
        double bestScore = Double.NEGATIVE_INFINITY; // 가장 높은 점수로 설정

        for (Quotation quotation : quotations) {
            Request request = quotation.getRequest();
            if (request == null || request.getShop() == null) {
                continue; // Shop 정보가 없으면 스킵
            }
            Shop shop = request.getShop();
            double shopPrice =
                    quotationReqMapper.extractTotalPriceFromJson(quotation.getPrice()); // 가격
            double shopRating = shop.getRating(); // 평점
            double shopDistance = calculateDistance(shop.getLat(), shop.getLon(), lat, lon); // 거리

            // 각 요소를 기반으로 점수 계산
            double priceScore = calculatePriceScore(shopPrice);
            double ratingScore = calculateRatingScore(shopRating);
            double distanceScore = calculateDistanceScore(shopDistance);

            // 가중치 적용 (가중치는 조정 가능)
            double totalScore = priceScore * 0.4 + ratingScore * 0.3 + distanceScore * 0.3;

            // 최고 점수를 가진 매장 업데이트
            if (totalScore > bestScore) {
                bestScore = totalScore;
                bestShop = new ShopBestResponse(shop.getName(), "shop_image_url"); // 임시 이미지
            }
        }
        return bestShop;
    }

    // 가격 점수 계산 (가격이 낮을수록 높은 점수)
    private double calculatePriceScore(double price) {
        return 100.0 / (price + 1); // 가격에 비례하여 점수 계산 (가격이 낮을수록 높은 점수)
    }

    // 평점 점수 계산 (평점이 높을수록 높은 점수)
    private double calculateRatingScore(double rating) {
        return rating * 10; // 평점 * 10 (예: 5점이면 50점)
    }

    // 거리 점수 계산 (거리가 가까울수록 높은 점수)
    private double calculateDistanceScore(double distance) {
        if (distance == 0) {
            return 100.0; // 거리가 0이면 최대 점수
        }
        return 100.0 / (distance + 1); // 거리가 가까울수록 높은 점수
    }

    private double calculateDistance(
            double shopLat, double shopLon, double requestLat, double requestLon) {
        double lat1 = requestLat;
        double lon1 = requestLon;

        double lat2 = shopLat;
        double lon2 = shopLon;

        // 지구 반지름 (미터 단위)
        double earthRadius = 6371000;

        // 위도, 경도를 라디안으로 변환
        double lat1Rad = Math.toRadians(lat1);
        double lon1Rad = Math.toRadians(lon1);
        double lat2Rad = Math.toRadians(lat2);
        double lon2Rad = Math.toRadians(lon2);

        // 주어진 쿼리문과 동일한 방식으로 거리 계산
        double dLon = lon2Rad - lon1Rad;
        double a =
                Math.cos(lat1Rad) * Math.cos(lat2Rad) * Math.cos(dLon)
                        + Math.sin(lat1Rad) * Math.sin(lat2Rad);

        // ACOS 사용
        double c = Math.acos(a);

        // 거리 반환 (미터 단위로 반환)
        return earthRadius * c; // 미터
    }
}
