package com.webatrio.testjava.mapStruct;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EvenementDTO {

    private int id;
    private String description;
    private Date dateDebut;
    private Date dateFin;
    private String lieu;
    private int capaciteMaximale;

    private List<ParticipantDTO> participant;

}
