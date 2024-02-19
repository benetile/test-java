package com.webatrio.testjava.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "evenement")
public class Evenement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "evenement_id")
    private int id;
    private String description;
    private Date dateDebut;
    private Date dateFin;
    private String lieu;
    @NotNull
    private int capaciteMaximale;

    @OneToMany(mappedBy = "evenement")
    @JsonIgnoreProperties("{evenement}")
    private List<Participant> participants;


}
