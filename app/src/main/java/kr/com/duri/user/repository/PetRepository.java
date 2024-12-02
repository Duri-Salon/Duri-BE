package kr.com.duri.user.repository;

import kr.com.duri.user.domain.entity.Pet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {}
