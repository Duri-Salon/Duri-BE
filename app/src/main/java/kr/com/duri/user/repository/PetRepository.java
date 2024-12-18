package kr.com.duri.user.repository;

import java.util.List;

import kr.com.duri.user.domain.entity.Pet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PetRepository extends JpaRepository<Pet, Long> {

    // 고객 ID 기준 반려견 찾기
    List<Pet> findByUserIdAndDeletedFalse(Long userId);

    @Query(
            "SELECT p FROM Feedback f "
                    + "JOIN f.quotation q "
                    + "JOIN q.request r "
                    + "JOIN r.quotation qReq "
                    + "JOIN qReq.pet p "
                    + "WHERE f.id = :feedbackId")
    Pet findByfeedbackId(@Param("feedbackId") Long feedbackId);
}
