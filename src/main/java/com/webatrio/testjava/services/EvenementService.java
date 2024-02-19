package com.webatrio.testjava.services;

import com.webatrio.testjava.exceptions.EvenementException;
import com.webatrio.testjava.interfaces.EvenementMapper;
import com.webatrio.testjava.mapStruct.EvenementDTO;
import com.webatrio.testjava.models.Evenement;
import com.webatrio.testjava.models.Participant;
import com.webatrio.testjava.repositories.EvenementRepository;
import com.webatrio.testjava.repositories.ParticipantRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class EvenementService {

    @Autowired
    private EvenementRepository evenementRepository;
    @Autowired
    private ParticipantRepository participantRepository;

    @Autowired
    private EvenementMapper evenementMapper;

    Logger logger = LoggerFactory.getLogger(EvenementService.class);

    public EvenementDTO traitementDeLEvenement(Evenement evenement){
        try {

            EvenementDTO evenementDTO= evenementMapper.toEvenementDto(evenement);
            evenement.setDescription(verificationDelaDescription(evenement.getDescription()));
            evenement.setLieu(verificationLieu(evenement.getLieu()));
            evenement.setCapaciteMaximale(verificationCapaciteMaximale(evenement.getCapaciteMaximale()));
            evenement.setDateDebut(verificationDateDebut(evenement.getDateDebut()));
            evenement.setDateFin(verificationDateFin(evenement.getDateDebut(),evenement.getDateFin()));
            return evenementDTO;
        }catch (EvenementException e){
            logger.error(e.getMessage());
        }
        return null;
    }
    public EvenementDTO creationEvenement(Evenement evenement) throws EvenementException{
        try {
            logger.info("Tentaive de création d'un événement");
            EvenementDTO evenementDTO = traitementDeLEvenement(evenement);
            if(evenementRepository.findByDescriptionIgnoreCaseAndLieuIgnoreCaseAndDateDebutEqualsAndDateFinEquals(evenementDTO.getDescription(),
                    evenementDTO.getLieu(),evenementDTO.getDateDebut(),evenementDTO.getDateFin()).isEmpty()){
                evenement.setDescription(evenement.getDescription().toLowerCase());

                evenement = evenementMapper.toEvenement(evenementDTO);
                evenementRepository.save(evenement);
                EvenementDTO saveDto = evenementMapper.toEvenementDto(evenement);
                logger.info("Fin de la création de l'événement avec succès");
                return saveDto;
            }else{
                throw new EvenementException("Un événement portant la même déscription au même lieu et à la même date existe déjà");
            }
        }catch (EvenementException e){
            logger.error(e.getMessage(),e);
        }
        return null;

    }
    public EvenementDTO modifierUnEvenement(Evenement updateEvenement, int id) {
        EvenementDTO evenementDTO =traitementDeLEvenement(updateEvenement);
        try {
            logger.info("Tentative de modification d'un événement ");
            Optional<Evenement> evenement = Optional.ofNullable(evenementRepository.findById(id)
                    .orElseThrow(() -> new EvenementException("Une erreur s'est produite lors la récupération de l'evenement")));

            evenementDTO = evenementMapper.toEvenementDto(updateEvenement);

            if(evenementRepository.findByDescriptionIgnoreCaseAndLieuIgnoreCaseAndDateDebutEqualsAndDateFinEquals(evenementDTO.getDescription(),
                    evenementDTO.getLieu(),evenementDTO.getDateDebut(),evenementDTO.getDateFin()).isEmpty()){
                Evenement event = evenementMapper.toEvenement(evenementDTO);
                evenementRepository.save(event);
                logger.info("Fin de la mise à jour de l'événemnt");
                return evenementMapper.toEvenementDto(event);
            }


        } catch (EvenementException e) {
            logger.error(e.getMessage(),e);
        }
        return evenementDTO;
    }
    public void annulerUnEvenement(int id){
        try {
            Optional<Evenement> evenement = Optional.ofNullable(evenementRepository.findById(id).
                    orElseThrow(() -> new EvenementException("Une erreur s'est produite lors la récupération de l'evenement")));
            evenementRepository.deleteById(id);
            logger.info("Annulation de l'événement a été éffectuée avec succès");
        }catch (EvenementException e){
            logger.error(e.getMessage(),e);
        }

    }
    public List<EvenementDTO> afficherLesEvenementsParIdParticipant(int id){
        List<EvenementDTO> dtos = new ArrayList<>();
        for (Evenement evenement : evenementRepository.findByParticipantsId(id)){
            EvenementDTO dto = evenementMapper.toEvenementDto(evenement);
            dtos.add(dto);
        }
        return dtos;
    }

    public List<EvenementDTO> afficherLesEvenementAVenir(){
        Date now = new Date();
        List<EvenementDTO> dtos = new ArrayList<>();
        for (Evenement evenement : evenementRepository.findByDateDebutAfter(now)){
            EvenementDTO dto = evenementMapper.toEvenementDto(evenement);
            dtos.add(dto);
        }
        return dtos;
    }

    public Optional<EvenementDTO> findById(int id ) throws EvenementException {
        Optional<Evenement> evenement = Optional.ofNullable(evenementRepository.findById(id).orElseThrow(() -> new EvenementException("")));
        EvenementDTO evenementDTO = evenementMapper.toEvenementDto(evenement.get());
        return Optional.ofNullable(evenementDTO);
    }

    public List<EvenementDTO> findByLieuIgnoreCase(String lieu){
        List<EvenementDTO> dtos = new ArrayList<>();
        for (Evenement evenement : evenementRepository.findByLieuIgnoreCase(lieu)){
            EvenementDTO dto = evenementMapper.toEvenementDto(evenement);
            dtos.add(dto);
        }
        return dtos;
    }
    public List<EvenementDTO> findAll(){
        List<EvenementDTO> evenementDTOS = new ArrayList<>();
        for (Evenement evenement : evenementRepository.findAll()){
            EvenementDTO dto = evenementMapper.toEvenementDto(evenement);
            evenementDTOS.add(dto);
        }
        return evenementDTOS;
    }

    public String verificationDelaDescription(String description) throws EvenementException{
        if(!description.isEmpty()){
            return description;
        } throw
                new EvenementException("Impossible de poursuivre la création l'évenement car aucune déscription ne renseignée");
    }

    public String verificationLieu(String lieu) throws EvenementException{
        if(!lieu.isEmpty()){
            return lieu;
        }throw new EvenementException("Impossible de poursuivre la création l'évenement car aucun lieu ne renseigné");
    }

    public int verificationCapaciteMaximale(int capacite) throws EvenementException{
        if(capacite > 0){
            return capacite;
        }throw new EvenementException("Impossible de poursuivre la création de l'événment car la capacité maximale ne renseignée");
    }

    public Date verificationDateDebut(Date date) throws EvenementException{
        Date now = new Date();
        if(date != null && date.after(now)){
            return date;
        }throw new EvenementException("Une erreur s'est produite car la date de début de l'événmént n'est pas correcte");
    }

    public Date verificationDateFin(Date debut, Date fin) throws EvenementException{
        if(fin != null && fin.after(debut)){
            return fin;
        }throw new EvenementException("Une erreur s'est produite lors de la validation de la date de fin ");
    }

    public EvenementDTO test() throws EvenementException {
        Evenement evenement = new Evenement();
        evenement.setDescription("Description TEST");
        evenement.setDateFin(new Date(2024,3,20,12,30));
        evenement.setDateDebut(new Date(2024,3,15,12,20));
        evenement.setCapaciteMaximale(1000);
        evenement.setLieu("SANNOIS");

        return creationEvenement(evenement);
    }
}
