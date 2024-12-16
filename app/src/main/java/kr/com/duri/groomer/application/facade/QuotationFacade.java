package kr.com.duri.groomer.application.facade;

import kr.com.duri.groomer.application.dto.request.QuotationRequest;
import kr.com.duri.groomer.application.dto.request.QuotationUpdateRequest;
import kr.com.duri.groomer.application.dto.response.QuotationDetailResponse;
import kr.com.duri.groomer.application.mapper.QuotationMapper;
import kr.com.duri.groomer.application.service.GroomerService;
import kr.com.duri.groomer.application.service.QuotationService;
import kr.com.duri.groomer.application.service.ShopImageService;
import kr.com.duri.groomer.domain.entity.Groomer;
import kr.com.duri.groomer.domain.entity.Quotation;
import kr.com.duri.groomer.domain.entity.Shop;
import kr.com.duri.groomer.domain.entity.ShopImage;
import kr.com.duri.user.application.service.PaymentService;
import kr.com.duri.user.application.service.RequestService;
import kr.com.duri.user.application.service.ReviewService;
import kr.com.duri.user.domain.Enum.PaymentStatus;
import kr.com.duri.user.domain.Enum.QuotationStatus;
import kr.com.duri.user.domain.entity.Payment;
import kr.com.duri.user.domain.entity.Pet;
import kr.com.duri.user.domain.entity.Request;
import kr.com.duri.user.domain.entity.Review;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuotationFacade {

    private final RequestService requestService;
    private final QuotationService quotationService;
    private final ShopImageService shopImageService;
    private final GroomerService groomerService;
    private final PaymentService paymentService;
    private final ReviewService reviewService;
    private final QuotationMapper quotationMapper;

    public void createQuotation(QuotationRequest quotationRequest) {
        // 1. 요청 ID로 Request 객체 조회
        Request request = requestService.getRequestById(quotationRequest.getRequestId());

        Quotation existingQuotation = quotationService.getByRequestId(request.getId());

        // 3. Quotation 엔티티 생성 (존재하지 않으면 새로 생성)
        Quotation quotation = null;
        if (existingQuotation == null) {
            // 기존 견적서가 없다면 새로 생성
            quotation =
                    quotationMapper.toQuotationEntity(
                            request, quotationRequest, quotationRequest.getPriceDetail());
        } else {
            if (existingQuotation.getRequest().getStatus() == QuotationStatus.APPROVED) {
                throw new IllegalStateException("이미 견적서가 작성되었습니다.");
            }
        }

        // 4. QuotationService를 사용하여 저장
        quotationService.saveQuotation(quotation);

        // 5. 요청 상태를 APPROVED로 업데이트
        requestService.updateRequestStatusToApproved(request.getId());
    }

    public void updateQuotation(Long quotationId, QuotationUpdateRequest quotationUpdateRequest) {
        // 1. 기존 견적서 엔티티 조회
        Quotation existingQuotation = quotationService.findQuotationById(quotationId);

        // 2. Mapper를 사용하여 엔티티 업데이트
        quotationMapper.updateQuotationEntity(existingQuotation, quotationUpdateRequest);

        // 3. 업데이트된 엔티티 저장
        quotationService.updateQuotation(existingQuotation);
    }

    public QuotationDetailResponse getQuotationDetail(Long requestId) {
        // 1. request 조회
        Request request = requestService.getRequestById(requestId);

        // 2. Shop 및 Groomer 조회
        Shop shop = request.getShop();
        ShopImage shopImage = shopImageService.getMainShopImage(shop);
        Groomer groomer = groomerService.getGroomerByShopId(shop.getId());

        // 3. Pet 및 Quotation 조회
        Pet pet = request.getQuotation().getPet();
        Quotation quotation = quotationService.findByRequestId(requestId);

        // 4. 결제 상태 확인 (null 처리 추가)
        Payment payment = paymentService.findByQuotationId(quotation.getId());
        String paymentStatus = null;
        if (payment != null && payment.getStatus() == PaymentStatus.SUCCESS) {
            paymentStatus = "결제 완료";
        }

        // 5. 리뷰 상태 확인 (null 처리 추가)
        Review review = reviewService.findByRequestId(requestId);
        String reviewStatus = review != null ? "리뷰 완료" : null;

        // 6. 상태 결정 (null 처리 추가)
        String status = "결제 전"; // 기본값은 "결제 전"
        if (reviewStatus != null) {
            status = reviewStatus; // 리뷰가 있으면 리뷰 상태
        } else if (paymentStatus != null) {
            status = paymentStatus; // 결제 상태가 있으면 결제 상태
        }

        // 7. QuotationDetailResponse 반환
        return quotationMapper.toQuotationDetailResponse(
                request, shop, shopImage, groomer, pet, quotation, status);
    }
}
