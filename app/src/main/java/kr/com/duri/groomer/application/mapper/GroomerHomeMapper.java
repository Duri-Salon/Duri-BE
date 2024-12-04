package kr.com.duri.groomer.application.mapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import kr.com.duri.groomer.application.dto.response.RecentProcedureResponse;
import kr.com.duri.groomer.application.dto.response.TodayScheduleResponse;
import kr.com.duri.groomer.domain.entity.Groomer;
import kr.com.duri.groomer.domain.entity.Quotation;
import kr.com.duri.user.domain.entity.Pet;
import kr.com.duri.user.domain.entity.SiteUser;

import org.springframework.stereotype.Component;

@Component
public class GroomerHomeMapper {
    // DateTime Format
    DateTimeFormatter fullFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("MM-dd");
    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    // (Pet, User, Quotation) Entity to RecentProcedureResponse DTO
    public RecentProcedureResponse toRecentProcedureResponse(
            Pet pet, SiteUser user, Quotation quotation) {
        LocalDateTime now = LocalDateTime.now();
        return RecentProcedureResponse.builder()
                .petId(pet.getId())
                .petName(pet.getName())
                .breed(pet.getBreed())
                .gender(String.valueOf(pet.getGender()))
                .age(pet.getAge())
                .weight(pet.getWeight())
                .memo(
                        quotation.getRequest().getQuotation().getEtc() == null
                                ? ""
                                : quotation.getRequest().getQuotation().getEtc())
                .userId(user.getId())
                .userPhone(user.getPhone())
                .quotationId(quotation.getId())
                .startTime(quotation.getStartDateTime().format(fullFormatter))
                .complete(quotation.isComplete())
                .isNow(
                        now.isAfter(quotation.getStartDateTime())
                                && now.isBefore(quotation.getEndDateTime()))
                .build();
    }

    // (Pet, Quotation, Groomer) Entity to TodayScheduleResponse DTO
    public TodayScheduleResponse toTodayScheduleResponse(
            Pet pet, Quotation quotation, Groomer groomer) {
        return TodayScheduleResponse.builder()
                .date(LocalDateTime.now().format(dayFormatter))
                .startTime(quotation.getStartDateTime().format(timeFormatter))
                .petId(pet.getId())
                .petName(pet.getName())
                .breed(pet.getBreed())
                .gender(String.valueOf(pet.getGender()))
                .weight(pet.getWeight())
                .groomerName(groomer.getName())
                .build();
    }
}