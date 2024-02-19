package com.webatrio.testjava.mapStruct;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ParticipantDTO {

    private int id;
    private String nom;
    private String prenom;
    private String email;

    private EvenementDTO evenement;

}
