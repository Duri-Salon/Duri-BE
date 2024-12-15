package kr.com.duri.groomer.application.facade;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import kr.com.duri.groomer.application.dto.request.NewFeedbackRequest;
import kr.com.duri.groomer.application.dto.response.*;
import kr.com.duri.groomer.application.mapper.FeedbackMapper;
import kr.com.duri.groomer.application.service.*;
import kr.com.duri.groomer.domain.Enum.Behavior;
import kr.com.duri.groomer.domain.Enum.Friendly;
import kr.com.duri.groomer.domain.Enum.Reaction;
import kr.com.duri.groomer.domain.entity.Feedback;
import kr.com.duri.groomer.domain.entity.FeedbackImage;
import kr.com.duri.groomer.domain.entity.Groomer;
import kr.com.duri.groomer.domain.entity.Quotation;
import kr.com.duri.user.application.service.PetService;
import kr.com.duri.user.application.service.SiteUserService;
import kr.com.duri.user.domain.entity.Pet;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
public class FeedbackFacade {

    private final ShopService shopService;

    private final GroomerService groomerService;

    private final SiteUserService siteUserService;

    private final PetService petService;

    private final QuotationService quotationService;

    private final FeedbackService feedbackService;

    private final FeedbackImageService feedbackImageService;

    private final FeedbackMapper feedbackMapper;

    public FeedbackDetailResponse createNewFeedback(
            String token,
            Long quotationId,
            NewFeedbackRequest newFeedbackRequest,
            List<MultipartFile> images) {
        Long shopId = shopService.getShopIdByToken(token);
        Groomer groomer = groomerService.getGroomerByShopId(shopId);
        Quotation quotation = quotationService.findQuotationById(quotationId);
        Feedback feedback = feedbackService.saveNewFeedback(quotation, groomer, newFeedbackRequest);
        feedbackImageService.saveFeedbackImages(feedback, images);
        List<String> imageUrls = feedbackImageService.findFeedbackImagesByFeedback(feedback);
        return feedbackMapper.toFeedbackDetailResponse(feedback, imageUrls);
    }

    public List<PortfolioListResponse> getPortfolioList(Long groomerId) {
        List<Feedback> feedbackList = feedbackService.getPortfolioList(groomerId);
        return feedbackList.stream()
                .map(
                        feedback -> {
                            FeedbackImage image =
                                    feedbackImageService.findFirstFeedbackImageByFeedback(
                                            feedback.getId());
                            if (image != null) {
                                return feedbackMapper.toPortfolioListResponse(feedback, image);
                            }
                            return null;
                        })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public PortfolioDetailResponse getPortfolioDetail(Long feedbackId) {
        Feedback feedback = feedbackService.getFeedbackById(feedbackId);
        List<String> imageUrls = feedbackImageService.findFeedbackImagesByFeedback(feedback);
        return feedbackMapper.toPortfolioDetailResponse(feedback, imageUrls);
    }

    public DiaryDetailResponse getDiaryDetail(Long quotationId) { // todo : 펫 프로필, 디자이너 프로필 같이 조회?
        Feedback feedback = feedbackService.getFeedbackByQuotationId(quotationId);
        List<String> imageUrls = feedbackImageService.findFeedbackImagesByFeedback(feedback);
        return feedbackMapper.toDiaryDetailResponse(feedback, imageUrls);
    }

    public FeedbackDataResponse getFeedbackData(String token) {
        Long siteuserId = siteUserService.getUserIdByToken(token);
        Pet pet = petService.getPetByUserId(siteuserId);
        List<Feedback> feedbackList = feedbackService.findAllByPet(pet.getId());

        if (feedbackList.isEmpty()) {
            throw new IllegalArgumentException("해당 반려견에 대한 피드백 데이터가 없습니다.");
        }

        String mostFriendly =
                feedbackService.getMostSelected(
                        feedbackList, Feedback::getFriendly, Friendly::getDescription);
        String mostReaction =
                feedbackService.getMostSelected(
                        feedbackList, Feedback::getReaction, Reaction::getDescription);
        String mostBehavior =
                feedbackService.getMostSelected(
                        feedbackList, Feedback::getBehavior, Behavior::getDescription);

        return feedbackMapper.toFeedbackDataResponse(mostFriendly, mostReaction, mostBehavior);
    }
}