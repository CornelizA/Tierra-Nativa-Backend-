package com.tierranativa.aplicacion.tierra.nativa.controller;

import com.tierranativa.aplicacion.tierra.nativa.dto.PackageTravelRequestDTO;
import com.tierranativa.aplicacion.tierra.nativa.service.SearchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SearchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SearchService searchService;

    private PackageTravelRequestDTO mockPackage;

    @BeforeEach
    void setUp() {
        mockPackage = PackageTravelRequestDTO.builder()
                .id(1L)
                .name("Glaciar Perito Moreno")
                .destination("El Calafate")
                .basePrice(690000.0)
                .build();
    }

    @Test
    void searchByKeyword_Success() throws Exception {
        Mockito.when(searchService.search(any())).thenReturn(List.of(mockPackage));

        mockMvc.perform(get("/search")
                        .param("keyword", "Calafate")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").value("Glaciar Perito Moreno"))
                .andExpect(jsonPath("$[0].destination").value("El Calafate"));
    }

    @Test
    void searchByDateRange_Success() throws Exception {
        Mockito.when(searchService.search(any())).thenReturn(List.of(mockPackage));

        mockMvc.perform(get("/search")
                        .param("keyword", "Patagonia")
                        .param("checkIn", "2026-10-01")
                        .param("checkOut", "2026-10-10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void searchWithNoResults_ReturnsEmptyList() throws Exception {
        Mockito.when(searchService.search(any())).thenReturn(List.of());

        mockMvc.perform(get("/search")
                        .param("keyword", "DestinoInexistente")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void searchWithNoParams_Success() throws Exception {
        Mockito.when(searchService.search(any())).thenReturn(List.of(mockPackage));

        mockMvc.perform(get("/search")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}