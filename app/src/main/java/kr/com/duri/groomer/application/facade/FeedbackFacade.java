package kr.com.duri.groomer.application.facade;

import kr.com.duri.groomer.application.dto.request.NewFeedbackRequest;
import kr.com.duri.groomer.application.dto.response.FeedbackDetailResponse;
import kr.com.duri.groomer.application.mapper.FeedbackMapper;
import kr.com.duri.groomer.application.service.*;
import kr.com.duri.groomer.domain.entity.Feedback;
import kr.com.duri.groomer.domain.entity.Groomer;
import kr.com.duri.groomer.domain.entity.Quotation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FeedbackFacade {

    private final ShopService shopService;

    private final GroomerService groomerService;

    private final QuotationService quotationService;

    private final FeedbackService feedbackService;

    private final FeedbackImageService feedbackImageService;

    private final FeedbackMapper feedbackMapper;

    public FeedbackDetailResponse createNewFeedback(
            String token, Long quotationId, NewFeedbackRequest newFeedbackRequest, List<MultipartFile> images) {
        Long shopId = shopService.getShopIdByToken(token);
        Groomer groomer = groomerService.getGroomerByShopId(shopId);
        Quotation quotation = quotationService.findQuotationById(quotationId);
        Feedback feedback = feedbackService.saveNewFeedback(quotation, groomer, newFeedbackRequest);
        feedbackImageService.saveFeedbackImages(feedback, images);
        List<String> imageUrls = feedbackImageService.findFeedbackImagesByFeedback(feedback);
        return feedbackMapper.toFeedbackDetailResponse(feedback, imageUrls);
    }
}
