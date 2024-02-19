package com.webatrio.testjava.interfaces;

import com.webatrio.testjava.mapStruct.EvenementDTO;
import com.webatrio.testjava.models.Evenement;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.Optional;

@Mapper(componentModel = "spring")
public interface EvenementMapper {

    Evenement toEvenement(EvenementDTO evenementDTO);

    EvenementDTO toEvenementDto(Evenement evenement);

    void copy(EvenementDTO evenementDTO, @MappingTarget Evenement evenement);
}
