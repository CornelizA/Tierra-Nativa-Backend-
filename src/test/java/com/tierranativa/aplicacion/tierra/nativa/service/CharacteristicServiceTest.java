package com.tierranativa.aplicacion.tierra.nativa.service;

import com.tierranativa.aplicacion.tierra.nativa.dto.CharacteristicDTO;
import com.tierranativa.aplicacion.tierra.nativa.entity.Characteristics;
import com.tierranativa.aplicacion.tierra.nativa.repository.CharacteristicRepository;
import com.tierranativa.aplicacion.tierra.nativa.service.implement.ICharacteristicService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CharacteristicServiceTest {

    @Mock
    private CharacteristicRepository characteristicRepository;

    @InjectMocks
    private ICharacteristicService characteristicService;

    @Test
    void save_Success() {
        Characteristics entity = Characteristics.builder()
                .title("Guías")
                .icon("users")
                .build();

        when(characteristicRepository.save(any(Characteristics.class))).thenReturn(entity);

        CharacteristicDTO result = characteristicService.save(entity);

        assertThat(result.getTitle()).isEqualTo("Guías");
        verify(characteristicRepository, times(1)).save(entity);
    }

    @Test
    void findById_Success() {
        Characteristics entity = Characteristics.builder()
                .id(1L)
                .title("Seguro")
                .icon("shield")
                .build();

        when(characteristicRepository.findById(1L)).thenReturn(Optional.of(entity));

        Optional<CharacteristicDTO> result = characteristicService.findById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getTitle()).isEqualTo("Seguro");
    }
}