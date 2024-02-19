package com.webatrio.testjava.repositories;

import com.webatrio.testjava.models.Evenement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface EvenementRepository extends JpaRepository<Evenement, Integer> {


    Optional<Evenement> findByDescriptionIgnoreCaseAndLieuIgnoreCaseAndDateDebutEqualsAndDateFinEquals(String desc, String lieu, Date debut, Date fin);

    List<Evenement> findByLieuIgnoreCase(String lieu);

    List<Evenement> findByParticipantsId(int idParticant);

    List<Evenement> findByDateDebutAfter(Date date);

}
