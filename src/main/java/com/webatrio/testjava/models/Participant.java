package com.webatrio.testjava.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "participant")
public class Participant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "participant_id")
    private int id;
    private String nom;
    private String prenom;
    @NotNull
    private String email;

    @ManyToOne
    @JoinColumn(name = "evenenemt_id")
    @JsonIgnoreProperties({"participants"})
    private Evenement evenement;

}
