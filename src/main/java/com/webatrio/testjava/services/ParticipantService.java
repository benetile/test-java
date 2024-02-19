package com.webatrio.testjava.services;

import com.webatrio.testjava.exceptions.EvenementException;
import com.webatrio.testjava.exceptions.ParticipantException;
import com.webatrio.testjava.interfaces.ParticipantMapper;
import com.webatrio.testjava.mapStruct.EvenementDTO;
import com.webatrio.testjava.mapStruct.ParticipantDTO;
import com.webatrio.testjava.models.Evenement;
import com.webatrio.testjava.models.Participant;
import com.webatrio.testjava.repositories.EvenementRepository;
import com.webatrio.testjava.repositories.ParticipantRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ParticipantService {

    @Autowired
    private ParticipantRepository participantRepository;
    @Autowired
    EvenementRepository evenementRepository;
    Logger logger = LoggerFactory.getLogger(ParticipantService.class);

    @Autowired
    private EvenementService evenementService;
    @Autowired
    private ParticipantMapper participantMapper;

    public ParticipantDTO creation(ParticipantDTO participantDTO) throws ParticipantException, EvenementException {

        Participant participant = traitementParticipant(participantDTO);
        Evenement evenement = participant.getEvenement();
        if(participantRepository.findByEmailAndEvenementId(participant.getEmail(), evenement.getId()).isEmpty()){
             participantRepository.save(participant);
             ParticipantDTO saveDto = participantMapper.toParticipantDto(participant);
             return saveDto;
        }throw new ParticipantException("Le participant est déjà inscrit dans cete évenement");
    }

    public void annulerParticipant(int idParticipant, int idEvenement){
        try {
            if(participantRepository.findByIdAndEvenementId(idParticipant, idEvenement).isPresent()){
                Participant participant = participantRepository.findById(idParticipant)
                        .orElseThrow(()-> new ParticipantException("Une erreur s'est produite lors de la récuperation du participant"));
                participant.setEvenement(null);
                participantRepository.save(participant);

                logger.info("Fin de la suppression de l'évènement ");
            }
        }catch (ParticipantException e){
            logger.error(e.getMessage(),e);
        }

    }

    public List<ParticipantDTO> afficherParticipantParEvenement(int idEv){
        List<ParticipantDTO> dtos = new ArrayList<>();
        for (Participant participant : participantRepository.findByEvenementId(idEv)){
            ParticipantDTO dto = participantMapper.toParticipantDto(participant);
            dtos.add(dto);
        }
        return dtos;
    }

    public Participant traitementParticipant(ParticipantDTO participantDTO) throws ParticipantException, EvenementException {

        Participant participant = participantMapper.toParticipant(participantDTO);

        participant.setEmail(verificationEmail(participantDTO.getEmail()));
        participant.setNom(verificationNom(participantDTO.getNom()));
        participant.setPrenom(verificationPrenom(participantDTO.getPrenom()));

        if(participant.getEvenement() != null){
            participant.setEvenement(verificationEvenement(participant.getEvenement()));
        }else{
            throw new EvenementException("Impossible de poursuivre car l'evenement est manquant ");
        }

        return participant;
    }

    public String verificationEmail(String email) throws ParticipantException{
        if(!email.isEmpty()){
            return email;
        }throw new ParticipantException("Une erreur lors de la création d'un participant car son email est vide ");
    }
    public String verificationNom(String nom) throws ParticipantException{
        if(!nom.isEmpty()){
            return nom;
        }throw new ParticipantException("Une erreur lors de la création d'un participant car son nom n'est pas renseigné");
    }
    public String verificationPrenom(String prenom) throws ParticipantException{
        if(!prenom.isEmpty()){
            return prenom;
        }throw new ParticipantException("Une erreur lors de la création d'un participant car son prénom n'est pas renseigné ");
    }

    public Evenement verificationEvenement(Evenement evenement) throws EvenementException{
        if(evenementRepository.findById(evenement.getId()).isPresent()){
            return evenementRepository.findById(evenement.getId()).orElseThrow(()-> new EvenementException("Une erreur s'est produite lors de la récupération de de l'événement "));
        }
        throw new EvenementException("Impossible de poursuivre car l'evenement est manquant");
    }

}
