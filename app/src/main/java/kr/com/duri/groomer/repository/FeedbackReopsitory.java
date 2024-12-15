package kr.com.duri.groomer.repository;

import kr.com.duri.groomer.domain.entity.Feedback;
import kr.com.duri.user.domain.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FeedbackReopsitory extends JpaRepository<Feedback, Long> {
    List<Feedback> findByGroomerIdAndExposeTrueAndDeletedFalseOrderByCreatedAtDesc(Long groomerId);

    Optional<Feedback> findByQuotationId(Long quotationId);

    @Query("SELECT f FROM Feedback f " +
            "JOIN f.quotation q " +
            "JOIN q.request r " +
            "JOIN r.quotation qReq " +
            "WHERE qReq.pet.id = :petId")
    List<Feedback> findAllByPetId(@Param("petId") Long petId);
}
