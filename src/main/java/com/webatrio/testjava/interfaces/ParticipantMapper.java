package com.webatrio.testjava.interfaces;

import com.webatrio.testjava.mapStruct.EvenementDTO;
import com.webatrio.testjava.mapStruct.ParticipantDTO;
import com.webatrio.testjava.models.Evenement;
import com.webatrio.testjava.models.Participant;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ParticipantMapper {

    Participant toParticipant(ParticipantDTO participantDTO);

    ParticipantDTO toParticipantDto(Participant participant);

}
