package com.tierranativa.aplicacion.tierra.nativa.service;

import com.tierranativa.aplicacion.tierra.nativa.dto.CharacteristicDTO;
import com.tierranativa.aplicacion.tierra.nativa.entity.Characteristics;

import java.util.List;
import java.util.Optional;

public interface CharacteristicService {

    CharacteristicDTO save(Characteristics characteristics);

    Optional<CharacteristicDTO> findById(Long id);

    CharacteristicDTO update(Characteristics characteristics);

    List<CharacteristicDTO> findAll();

    void deleteById(Long id);
}
