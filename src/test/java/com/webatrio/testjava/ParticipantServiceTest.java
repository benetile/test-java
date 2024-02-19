package com.webatrio.testjava;

import com.webatrio.testjava.exceptions.EvenementException;
import com.webatrio.testjava.exceptions.ParticipantException;
import com.webatrio.testjava.mapStruct.EvenementDTO;
import com.webatrio.testjava.mapStruct.ParticipantDTO;
import com.webatrio.testjava.models.Evenement;
import com.webatrio.testjava.models.Participant;
import com.webatrio.testjava.repositories.ParticipantRepository;
import com.webatrio.testjava.services.EvenementService;
import com.webatrio.testjava.services.ParticipantService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ParticipantServiceTest {

    @Autowired
    private ParticipantService participantService;
    @Autowired
    private ParticipantRepository participantRepository;
    @Autowired
    private EvenementService evenementService;


    @Test
    public void creerUnParticipant() throws EvenementException, ParticipantException {
        EvenementDTO evenementDTO = evenementService.findById(1).orElseThrow(()-> new EvenementException(""));
        ParticipantDTO participantDTO = new ParticipantDTO();
        participantDTO.setEmail("test@gmail.com");
        participantDTO.setNom("benny");
        participantDTO.setPrenom("test");
        participantDTO.setEvenement(evenementDTO);

        ParticipantDTO saveDto = participantService.creation(participantDTO);

        assertNotNull(saveDto.getId());
    }

    @Test
    public void afficherLesparticipantsDUnEvenement() throws EvenementException, ParticipantException {
        EvenementDTO evenementDTO = evenementService.findById(1).orElseThrow(()-> new EvenementException(""));

        for (int i = 0; i < 5; i++) {
            ParticipantDTO participantDTO = new ParticipantDTO();
            participantDTO.setEmail("test"+i+"-"+new Date()+"@gmail.com");
            participantDTO.setNom("benny "+i);
            participantDTO.setPrenom("test " +i);
            participantDTO.setEvenement(evenementDTO);

            participantService.creation(participantDTO);

        }

        List<Participant> participants = participantRepository.findByEvenementId(1);
        assertTrue(participants.size() > 5);
    }

    @Test
    public void annulerParticipantDUnEvenement() throws ParticipantException {
        Participant participant = participantRepository.findById(1).orElseThrow(()-> new ParticipantException(""));
        Evenement evenement = participant.getEvenement();
        participantService.annulerParticipant(participant.getId(), evenement.getId());

        Participant part = participantRepository.findById(1).orElseThrow(()-> new ParticipantException(""));
        assertNull(part.getEvenement());
    }
}
