package com.tierranativa.aplicacion.tierra.nativa.service.implement;

import com.tierranativa.aplicacion.tierra.nativa.dto.CharacteristicDTO;
import com.tierranativa.aplicacion.tierra.nativa.entity.PackageCharacteristics;
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

    public List<CharacteristicDTO> findAll() {
        List<PackageCharacteristics> characteristics = characteristicRepository.findAll();
        return CharacteristicDTO.fromEntityList(characteristics);
    }

    public CharacteristicDTO save(PackageCharacteristics packageCharacteristics) {
        if (packageCharacteristics.getId() != null) {
            if (characteristicRepository.existsById(packageCharacteristics.getId())) {
                throw new IllegalArgumentException("Ya existe una característica con el ID: " + packageCharacteristics.getId());
            }
        }
        if (characteristicRepository.findByTitle(packageCharacteristics.getTitle()).isPresent()) {
            throw new IllegalArgumentException("Ya existe una característica con el titulo: " + packageCharacteristics.getTitle());
        }

        PackageCharacteristics saved = characteristicRepository.save(packageCharacteristics);
        return CharacteristicDTO.fromEntity(saved);
    }

    @Override
    public Optional<CharacteristicDTO> findById(Long id) {
        return characteristicRepository.findById(id)
                .map(CharacteristicDTO::fromEntity);
    }

    @Override
    public CharacteristicDTO update(PackageCharacteristics packageCharacteristics) {
        if (packageCharacteristics.getId() == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo para actualizar.");
        }
        PackageCharacteristics updatedEntity = characteristicRepository.findById(packageCharacteristics.getId())
                .map(existing -> {
                    existing.setTitle(packageCharacteristics.getTitle());
                    existing.setIcon(packageCharacteristics.getIcon());
                    return characteristicRepository.save(existing);
                })
                .orElseThrow(() -> new ResourceNotFoundException("La característica con ID " + packageCharacteristics.getId() + " no fue encontrada."));

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

