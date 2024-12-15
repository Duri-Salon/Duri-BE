package kr.com.duri.groomer.application.service;

import java.util.List;
import java.util.function.Function;

import kr.com.duri.groomer.application.dto.request.NewFeedbackRequest;
import kr.com.duri.groomer.domain.entity.Feedback;
import kr.com.duri.groomer.domain.entity.Groomer;
import kr.com.duri.groomer.domain.entity.Quotation;

public interface FeedbackService {
    Feedback saveNewFeedback(
            Quotation quotation, Groomer groomer, NewFeedbackRequest newFeedbackRequest);

    List<Feedback> getPortfolioList(Long groomerId);

    Feedback getFeedbackById(Long feedbackId);

    Feedback getFeedbackByQuotationId(Long quotationId);

    List<Feedback> findAllByPet(Long petId);

    <T extends Enum<T>> String getMostSelected(
            List<Feedback> feedbackList,
            Function<Feedback, T> categoryMapper,
            Function<T, String> descriptionMapper);
}
