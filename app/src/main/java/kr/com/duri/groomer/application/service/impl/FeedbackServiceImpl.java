package kr.com.duri.groomer.application.service.impl;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import kr.com.duri.common.exception.NotFoundException;
import kr.com.duri.groomer.application.dto.request.NewFeedbackRequest;
import kr.com.duri.groomer.application.dto.request.PortfolioUpdateRequest;
import kr.com.duri.groomer.application.service.FeedbackService;
import kr.com.duri.groomer.domain.entity.Feedback;
import kr.com.duri.groomer.domain.entity.Groomer;
import kr.com.duri.groomer.domain.entity.Quotation;
import kr.com.duri.groomer.repository.FeedbackReopsitory;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackReopsitory feedbackReopsitory;

    @Override
    public Feedback getFeedbackById(Long feedbackId) {
        return feedbackReopsitory
                .findById(feedbackId)
                .orElseThrow(() -> new NotFoundException("피드백이 존재하지 않습니다."));
    }

    @Override
    public Feedback getFeedbackByQuotationId(Long quotationId) {
        return feedbackReopsitory
                .findByQuotationId(quotationId)
                .orElseThrow(() -> new NotFoundException("피드백이 존재하지 않습니다."));
    }

    @Override
    public List<Feedback> findAllByPet(Long petId) {
        return feedbackReopsitory.findAllByPetId(petId);
    }

    @Override
    public <T extends Enum<T>> String getMostSelected(
            List<Feedback> feedbackList,
            Function<Feedback, T> categoryMapper,
            Function<T, String> descriptionMapper) {
        Map<String, Long> frequencyMap =
                feedbackList.stream()
                        .map(categoryMapper)
                        .map(descriptionMapper)
                        .collect(Collectors.groupingBy(value -> value, Collectors.counting()));

        return frequencyMap.entrySet().stream()
                .sorted(
                        Map.Entry.<String, Long>comparingByValue()
                                .reversed() // 빈도 내림차순
                                .thenComparing(Map.Entry.comparingByKey())) // 사전순
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
    }

    @Override
    public void removePortfolio(Feedback feedback) {
        feedbackReopsitory.save(feedback.removed());
    }

    @Override
    public void updatePortfolio(Feedback feedback, PortfolioUpdateRequest updatePortfolioContent) {
        feedbackReopsitory.save(feedback.updatePortfolio(updatePortfolioContent.getPortfolioContent()));
    }

    @Override
    public Feedback saveNewFeedback(
            Quotation quotation, Groomer groomer, NewFeedbackRequest newFeedbackRequest) {
        return feedbackReopsitory.save(
                Feedback.createNewFeedback(
                        quotation,
                        groomer,
                        newFeedbackRequest.getFriendly(),
                        newFeedbackRequest.getReaction(),
                        newFeedbackRequest.getBehavior(),
                        newFeedbackRequest.getNoticeContent(),
                        newFeedbackRequest.getPortfolioContent(),
                        newFeedbackRequest.getExpose()));
    }

    @Override
    public List<Feedback> getPortfolioList(Long groomerId) {
        return feedbackReopsitory.findByGroomerIdAndExposeTrueAndDeletedFalseOrderByCreatedAtDesc(
                groomerId);
    }
}
