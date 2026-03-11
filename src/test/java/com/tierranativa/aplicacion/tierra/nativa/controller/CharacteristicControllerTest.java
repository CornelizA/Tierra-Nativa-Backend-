package com.tierranativa.aplicacion.tierra.nativa.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tierranativa.aplicacion.tierra.nativa.dto.CharacteristicDTO;
import com.tierranativa.aplicacion.tierra.nativa.entity.Characteristics;
import com.tierranativa.aplicacion.tierra.nativa.service.implement.ICharacteristicService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CharacteristicControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ICharacteristicService characteristicService;

    private Characteristics mockEntity;
    private CharacteristicDTO mockDto;

    @BeforeEach
    void setUp() {
        mockEntity = Characteristics.builder()
                .id(1L)
                .title("Wi-Fi")
                .icon("wifi")
                .build();

        mockDto = CharacteristicDTO.builder()
                .id(1L)
                .title("Wi-Fi")
                .icon("wifi")
                .build();
    }

    @Test
    void findAllCharacteristicPublic_Success() throws Exception {
        when(characteristicService.findAll()).thenReturn(List.of(mockDto));

        mockMvc.perform(get("/characteristics")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Wi-Fi"));
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    void createCharacteristic_Success() throws Exception {
        when(characteristicService.save(any(Characteristics.class))).thenReturn(mockDto);

        mockMvc.perform(post("/characteristics")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockEntity)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Wi-Fi"));
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    void deleteByID_Success() throws Exception {
        Mockito.doNothing().when(characteristicService).deleteById(1L);

        mockMvc.perform(delete("/characteristics/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void findAll_Unauthorized() throws Exception {
        mockMvc.perform(get("/characteristics"))
                .andExpect(status().isOk());
    }
}