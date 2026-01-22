package com.tierranativa.aplicacion.tierra.nativa.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tierranativa.aplicacion.tierra.nativa.dto.CharacteristicDTO;
import com.tierranativa.aplicacion.tierra.nativa.entity.PackageCharacteristics;
import com.tierranativa.aplicacion.tierra.nativa.service.implement.ICharacteristicService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CharacteristicControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ICharacteristicService iCharacteristicService;

    @InjectMocks
    private CharacteristicController characteristicController;

    private ObjectMapper objectMapper = new ObjectMapper();
    private CharacteristicDTO mockDto;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(characteristicController).build();
        mockDto = CharacteristicDTO.builder()
                .id(1L)
                .title("Wi-Fi")
                .icon("wifi")
                .build();
    }

    @Test
    void findAllCharacteristicPublic_Success() throws Exception {
        when(iCharacteristicService.findAll()).thenReturn(List.of(mockDto));

        mockMvc.perform(get("/characteristics/public"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Wi-Fi"));
    }

    @Test
    void createCharacteristics_Success() throws Exception {
        PackageCharacteristics entity = new PackageCharacteristics();
        entity.setTitle("Wi-Fi");
        entity.setIcon("wifi");

        when(iCharacteristicService.save(any(PackageCharacteristics.class))).thenReturn(mockDto);

        mockMvc.perform(post("/characteristics")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(entity)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void deleteByID_Success() throws Exception {
        doNothing().when(iCharacteristicService).deleteById(1L);

        mockMvc.perform(delete("/characteristics/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Se eliminó de forma correcta la característica con el Id: 1"));
    }
}