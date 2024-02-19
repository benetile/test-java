package com.webatrio.testjava.controllers;

import com.webatrio.testjava.exceptions.EvenementException;
import com.webatrio.testjava.exceptions.ParticipantException;
import com.webatrio.testjava.mapStruct.ParticipantDTO;
import com.webatrio.testjava.models.Participant;
import com.webatrio.testjava.repositories.ParticipantRepository;
import com.webatrio.testjava.services.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/participant")
public class ParticipantController {

    @Autowired
    private ParticipantRepository participantRepository;
    @Autowired
    private ParticipantService participantService;

    @PostMapping("/add")
    public ResponseEntity<?> creation(@RequestBody ParticipantDTO participantDTO, BindingResult result) throws EvenementException, ParticipantException {
        if(!result.hasErrors()){
            ParticipantDTO dto = participantService.creation(participantDTO);
            return ResponseEntity.ok(dto);
        }else{
            return ResponseEntity.badRequest().body("Une erreur s'est produite lors de la création du participant");
        }
    }


    @GetMapping("/evenement/{id}")
    public ResponseEntity<List<?>> afficherLesParticipantParEvenement(@PathVariable("id") int id ){
        List<ParticipantDTO> participants = participantService.afficherParticipantParEvenement(id);
        return ResponseEntity.ok(participants);
    }

    @GetMapping("/")
    public ResponseEntity<List<?>> afficherTousLesParticipants(){
        List<Participant> participantDTOS = participantRepository.findAll();
        return ResponseEntity.ok(participantDTOS);
    }

    @DeleteMapping("/annuler/{idPart}/{idEvent}")
    public void supprimer(@PathVariable("idPart") int idPart , @PathVariable("idEvent") int idEvent){
        participantService.annulerParticipant(idPart, idEvent);
    }

}
