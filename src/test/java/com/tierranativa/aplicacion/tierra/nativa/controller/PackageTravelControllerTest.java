package com.tierranativa.aplicacion.tierra.nativa.controller;

import com.tierranativa.aplicacion.tierra.nativa.dto.ImageDTO;
import com.tierranativa.aplicacion.tierra.nativa.dto.ItineraryDetailDTO;
import com.tierranativa.aplicacion.tierra.nativa.dto.PackageTravelRequestDTO;
import com.tierranativa.aplicacion.tierra.nativa.entity.*;
import com.tierranativa.aplicacion.tierra.nativa.service.implement.IPackageTravelService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class PackageTravelControllerTest {

    @Mock
    private IPackageTravelService packageTravelService;

    @InjectMocks
    private PackageTravelController packageTravelController;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();
    private PackageTravel mockPackage;
    private String packageTravelJson;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(packageTravelController).build();
        mockPackage = new PackageTravel();
        mockPackage.setId(1L);
        mockPackage.setName("Aventura Simplificada");
        mockPackage.setDestination("Patagonia");
        mockPackage.setBasePrice(1500.00);

        ItineraryDetailDTO mockItineraryDetail = new ItineraryDetailDTO();
        mockItineraryDetail.setDuration("7 Días");
        mockItineraryDetail.setLodgingType("Hotel de Montaña");
        mockItineraryDetail.setTransferType("Vuelos-Terrestres");
        mockItineraryDetail.setDailyActivitiesDescription("Actividades diarias");
        mockItineraryDetail.setFoodAndHydrationNotes("Alimentación variada de acuerdo a la necesidad");
        mockItineraryDetail.setGeneralRecommendations("Diviertanse");

        ImageDTO mockImage = new ImageDTO();
        mockImage.setUrl("https://ruta/imagen_principal.jpg");
        mockImage.setPrincipal(true);

        PackageTravelRequestDTO validDto = new PackageTravelRequestDTO(
                "Paquete Simplificado",
                mockItineraryDetail,
                1500.00,
                List.of(mockImage),
                "Desc",
                "Patagonia",
                "GEOPAISAJES"
        );

        try {
            packageTravelJson = objectMapper.writeValueAsString(validDto);
        } catch (Exception e) {
            throw new RuntimeException("Error al serializar DTO: " + e.getMessage(), e);
        }
    }

    @Test
    void testFindAllPackageTravel() throws Exception {
        when(packageTravelService.findAll()).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/paquetes")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void testFindByIdPackageTravel() throws Exception {
        when(packageTravelService.findById(1L)).thenReturn(Optional.of(mockPackage));
        mockMvc.perform(get("/paquetes/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Aventura Simplificada"))
                .andExpect(jsonPath("$.destination").value("Patagonia"));
    }

    @Test
    void testFindByIdPackageTravelNoExist() throws Exception {
        when(packageTravelService.findById(99L)).thenReturn(Optional.empty());
        mockMvc.perform(get("/paquetes/99")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testPostRegisterPackage() throws Exception {
        when(packageTravelService.registerNewPackage(any(PackageTravelRequestDTO.class))).thenReturn(mockPackage);
        mockMvc.perform(post("/paquetes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(packageTravelJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Aventura Simplificada"));
    }

    @Test
    void testDeletePackageTravel() throws Exception {
        doNothing().when(packageTravelService).delete(1L);
        mockMvc.perform(delete("/paquetes/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Se eliminó de forma correcta el paquete de viaje con el Id: 1"));

        verify(packageTravelService, times(1)).delete(1L);
    }

    @Test
    void testFindByCategory() throws Exception {
        when(packageTravelService.findByCategory(PackageCategory.GEOPAISAJES)).thenReturn(List.of(mockPackage));
        mockMvc.perform(get("/paquetes/categoria/GEOPAISAJES")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].destination").value("Patagonia"));
    }
}
