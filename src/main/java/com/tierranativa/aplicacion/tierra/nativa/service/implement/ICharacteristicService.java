package com.tierranativa.aplicacion.tierra.nativa.service.implement;

import com.tierranativa.aplicacion.tierra.nativa.dto.CharacteristicDTO;
import com.tierranativa.aplicacion.tierra.nativa.entity.Characteristics;
import com.tierranativa.aplicacion.tierra.nativa.exception.ResourceNotFoundException;
import com.tierranativa.aplicacion.tierra.nativa.repository.CharacteristicRepository;
import com.tierranativa.aplicacion.tierra.nativa.service.CharacteristicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ICharacteristicService implements CharacteristicService {

    private final CharacteristicRepository characteristicRepository;

    @Autowired
    public ICharacteristicService(CharacteristicRepository characteristicRepository) {
        this.characteristicRepository = characteristicRepository;
    }

    @Override
    public List<CharacteristicDTO> findAll() {
        List<Characteristics> characteristics = characteristicRepository.findAll();
        return CharacteristicDTO.fromEntityList(characteristics);
    }

    @Override
    public CharacteristicDTO save(Characteristics characteristics) {
        if (characteristics.getId() != null) {
            if (characteristicRepository.existsById(characteristics.getId())) {
                throw new IllegalArgumentException("Ya existe una característica con el ID: " + characteristics.getId());
            }
        }
        if (characteristicRepository.findByTitle(characteristics.getTitle()).isPresent()) {
            throw new IllegalArgumentException("Ya existe una característica con el titulo: " + characteristics.getTitle());
        }
        Characteristics saved = characteristicRepository.save(characteristics);
        return CharacteristicDTO.fromEntity(saved);
    }

    @Override
    public Optional<CharacteristicDTO> findById(Long id) {
        return characteristicRepository.findById(id)
                .map(CharacteristicDTO::fromEntity);
    }

    @Override
    public CharacteristicDTO update(Characteristics characteristics) {
        if (characteristics.getId() == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo para actualizar.");
        }
        Characteristics updatedEntity = characteristicRepository.findById(characteristics.getId())
                .map(existing -> {
                    existing.setTitle(characteristics.getTitle());
                    existing.setIcon(characteristics.getIcon());
                    return characteristicRepository.save(existing);
                })
                .orElseThrow(() -> new ResourceNotFoundException("La característica con ID " + characteristics.getId() + " no fue encontrada."));

        return CharacteristicDTO.fromEntity(updatedEntity);
    }

    @Override
    public void deleteById(Long id) {
        if (!characteristicRepository.existsById(id)) {
            throw new IllegalArgumentException("La característica con ID " + id + " no existe.");
        }
        characteristicRepository.deleteById(id);
    }
}

