package kr.com.duri.user.application.facade;

import java.time.LocalDateTime;
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
                            LocalDateTime createdAt = quotationReq.getCreatedAt();
                            LocalDateTime expiredAt = createdAt.plusHours(24);

                            List<Request> requests =
                                    requestService.findByQuotationId(quotationReq.getId());

                            List<QuotationListShopResponse> shops =
                                    requests.stream()
                                            .map(
                                                    request -> {
                                                        Shop shop =
                                                                shopService.findById(
                                                                        request.getShop().getId());
                                                        return QuotationListShopResponse.builder()
                                                                .shopId(shop.getId())
                                                                .shopName(shop.getName())
                                                                .build();
                                                    })
                                            .collect(Collectors.toList());

                            return QuotationListResponse.builder()
                                    .quotationReqId(quotationReq.getId())
                                    .createdAt(createdAt)
                                    .expiredAt(expiredAt)
                                    .shops(shops)
                                    .build();
                        })
                .collect(Collectors.toList());
    }
}
