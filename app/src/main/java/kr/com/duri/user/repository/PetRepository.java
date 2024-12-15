package kr.com.duri.user.repository;

import java.util.List;

import kr.com.duri.user.domain.entity.Pet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PetRepository extends JpaRepository<Pet, Long> {

    // 고객 ID 기준 반려견 찾기
    List<Pet> findByUserId(Long userId);

    @Query(
            "SELECT f FROM Feedback f "
                    + "JOIN f.quotation q "
                    + "JOIN q.request r "
                    + "JOIN r.quotation qReq "
                    + "WHERE qReq.pet.id = :petId")
    Pet findByQuotationId(Long quotationId);

    /*
        @Query(
            "SELECT f FROM Feedback f "
                    + "JOIN f.quotation q "
                    + "JOIN q.request r "
                    + "JOIN r.quotation qReq "
                    + "WHERE qReq.pet.id = :petId")
    List<Feedback> findAllByPetId(@Param("petId") Long petId);
     */
}
