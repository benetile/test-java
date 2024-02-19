package com.webatrio.testjava.repositories;

import com.webatrio.testjava.mapStruct.ParticipantDTO;
import com.webatrio.testjava.models.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Integer> {

    Optional<Participant> findByEmailAndEvenementId(String email, int id );

    List<Participant> findByEvenementId(int id);

    Optional<Participant> findByIdAndEvenementId(int idParticipant, int IdEvenement);
}
