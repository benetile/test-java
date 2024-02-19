package com.webatrio.testjava;

import com.webatrio.testjava.exceptions.EvenementException;
import com.webatrio.testjava.mapStruct.EvenementDTO;
import com.webatrio.testjava.models.Evenement;
import com.webatrio.testjava.repositories.EvenementRepository;
import com.webatrio.testjava.services.EvenementService;
import org.hibernate.sql.Update;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class EvenementServiceTest {

    @Autowired
    private EvenementService evenementService;

    @Autowired
    private EvenementRepository evenementRepository;

    @Test
    public void creationEvenementTest() throws EvenementException {
        Evenement evenement = Evenement.builder().description("Description pour l'evenement du test fonctionnel").capaciteMaximale(20000)
                .lieu("Bercy").dateDebut(new Date(2024,06,30,20,00)).dateFin(new Date(2024,07,1,12,00)).build();

        EvenementDTO save = evenementService.creationEvenement(evenement);

        assertNotNull(save.getId());
    }

    @Test
    public void modifierEvenementTest() throws EvenementException {
        Evenement evenement = evenementRepository.findById(1).orElseThrow(()-> new EvenementException("Une erreur s'est produite lors de la recuperartion de l'evenement"));

        Evenement update=evenement;

        int capacite = 25000;
        String description ="Modification de la déscription pour l'événment de Bercy "+new Date();

        update.setCapaciteMaximale(capacite);

        update.setDescription(description);

        evenementService.modifierUnEvenement(update, evenement.getId());

        EvenementDTO dto = evenementService.findById(evenement.getId())
                .orElseThrow(()-> new EvenementException("Une erreur s'est produite lors de la recuperartion de l'evenement"));

        assertEquals(dto.getDescription(),description);
        assertEquals(capacite, dto.getCapaciteMaximale());
    }

    @Test
    public void afficherEvenemtParLieu() throws EvenementException {
        String lieu = "Stade de France";
        Evenement evenement = Evenement.builder().description("Description pour l'evenement du pour le stade").capaciteMaximale(80000)
                .lieu(lieu).dateDebut(new Date(2024,06,30,20,00)).dateFin(new Date(2024,07,1,12,00)).build();

        evenementService.creationEvenement(evenement);

        List<EvenementDTO> evenementDTOS = evenementService.findByLieuIgnoreCase(lieu);

        assertTrue(!evenementDTOS.isEmpty());


    }
}
