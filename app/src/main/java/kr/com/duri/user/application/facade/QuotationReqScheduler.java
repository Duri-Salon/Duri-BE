package kr.com.duri.user.application.facade;

import java.time.LocalDateTime;
import java.util.List;

import kr.com.duri.user.application.service.QuotationReqService;
import kr.com.duri.user.application.service.RequestService;
import kr.com.duri.user.domain.entity.QuotationReq;
import kr.com.duri.user.domain.entity.Request;
import lombok.RequiredArgsConstructor;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class QuotationReqScheduler {
    private final QuotationReqService quotationReqService;
    private final RequestService requestService;

    @Transactional
    @Scheduled(cron = "0 * * * * *")
    public void closeExpiredQuotationReqs() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime thresholdTime = now.minusHours(24);

        List<QuotationReq> expiredQuotationReqs =
                quotationReqService.findExpiredQuotationReqs(thresholdTime);

        for (QuotationReq quotationReq : expiredQuotationReqs) {
            if (quotationReq.getCreatedAt().isBefore(thresholdTime)
                    && quotationReq.getClose() == false) {
                // QuotationReq의 close 상태를 true로 설정
                quotationReqService.closeQuotationReq(quotationReq);

                // 해당 QuotationReq와 연관된 Request의 상태를 'EXPIRED'로 변경
                List<Request> relatedRequests =
                        requestService.findRequestsByQuotation(quotationReq);
                relatedRequests.forEach(
                        request -> {
                            requestService.updateRequestStatusToExpired(
                                    request); // 'EXPIRED' 상태로 변경
                        });
            }
        }
    }
}
