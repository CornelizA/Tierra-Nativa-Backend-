package com.tierranativa.aplicacion.tierra.nativa.service;

import com.tierranativa.aplicacion.tierra.nativa.dto.CharacteristicDTO;
import com.tierranativa.aplicacion.tierra.nativa.entity.PackageCharacteristics;

import java.util.List;
import java.util.Optional;

public interface CharacteristicService {

    CharacteristicDTO save(PackageCharacteristics packageCharacteristics);

    Optional<CharacteristicDTO> findById(Long id);

    CharacteristicDTO update(PackageCharacteristics packageCharacteristics);

    List<CharacteristicDTO> findAll();

    void deleteById(Long id);
}
