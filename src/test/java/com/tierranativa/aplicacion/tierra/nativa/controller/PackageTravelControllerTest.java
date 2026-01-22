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
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(MockitoExtension.class)
class PackageTravelControllerTest {

    @Mock
    private IPackageTravelService iPackageTravelService;

    @InjectMocks
    private PackageTravelController packageTravelController;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();
    private PackageTravel mockPackage;
    private String packageTravelJson;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(packageTravelController).build();

        Category geoPaisajes = Category.builder()
                .title("GEOPAISAJES")
                .description("Viajes enfocados en paisajes naturales únicos.")
                .build();


        String act1 = "Día 1: Llegada a El Calafate...";
        String food1 = "Incluye todas las comidas...";
        PackageItineraryDetail detail1 = PackageItineraryDetail.builder()
                .duration("4 Días / 3 Noches")
                .lodgingType("Hotel 4 estrellas en El Calafate.")
                .transferType("Vuelo a FTE. Traslados.")
                .dailyActivitiesDescription(act1)
                .foodAndHydrationNotes(food1)
                .generalRecommendations("El clima es frío...")
                .build();

        mockPackage = new PackageTravel();
        mockPackage.setId(1L);
        mockPackage.setName("Glaciar Perito Moreno: Hielo Milenario");
        mockPackage.setShortDescription("Un viaje inolvidable...");
        mockPackage.setItineraryDetail(detail1);
        mockPackage.setBasePrice(690000.00);
        mockPackage.setDestination("El Calafate");
        mockPackage.setCategories(java.util.Set.of(geoPaisajes)); // Uso de Set
        mockPackage.setCharacteristics(new java.util.HashSet<>());
        mockPackage.addImage(PackageImage.builder()
                .url("https://images.pexels.com/photos/17217435/...")
                .principal(true)
                .build());

        ItineraryDetailDTO itineraryDTO = new ItineraryDetailDTO();
        itineraryDTO.setDuration("4 Días / 3 Noches");
        itineraryDTO.setLodgingType("Hotel 4 estrellas.");
        itineraryDTO.setLodgingType("Vuelo a FTE. Traslados.");
        itineraryDTO.setDailyActivitiesDescription(act1);
        itineraryDTO.setFoodAndHydrationNotes(food1);
        itineraryDTO.setGeneralRecommendations("El clima es frío...");

        ImageDTO imageDTO = new ImageDTO();
        imageDTO.setUrl("url");
        imageDTO.setPrincipal(true);

        PackageTravelRequestDTO validDto = new PackageTravelRequestDTO();
        validDto.setName("Glaciar Perito Moreno: Hielo Milenario");
        validDto.setItineraryDetail(itineraryDTO);
        validDto.setBasePrice(690000.00);
        validDto.setImageDetails(java.util.List.of(imageDTO));
        validDto.setShortDescription("Descripción corta");
        validDto.setDestination("El Calafate");
        validDto.setCategoryId(Set.of(1L));

        try {
            packageTravelJson = objectMapper.writeValueAsString(validDto);
        } catch (Exception e) {
            throw new RuntimeException("Error al serializar el DTO", e);
        }
    }

    @Test
    void testGetAllPackagesPublic() throws Exception {
        when(iPackageTravelService.findAll()).thenReturn(List.of(mockPackage));

        mockMvc.perform(get("/paquetes")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").value("Glaciar Perito Moreno: Hielo Milenario"));
    }

    @Test
    void testGetAllPackagesForAdmin() throws Exception {
        when(iPackageTravelService.findAll()).thenReturn(List.of(mockPackage));

        mockMvc.perform(get("/paquetes/admin")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void testFindById_Success() throws Exception {
        when(iPackageTravelService.findById(1L)).thenReturn(Optional.of(mockPackage));

        mockMvc.perform(get("/paquetes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Glaciar Perito Moreno: Hielo Milenario"));
    }

    @Test
    void testFindById_NotFound() throws Exception {
        when(iPackageTravelService.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/paquetes/99"))
                .andExpect(status().isNotFound());
    }

    // --- PRUEBAS POST/PUT ---

    @Test
    void testRegisterPackage() throws Exception {
        when(iPackageTravelService.registerNewPackage(any(PackageTravelRequestDTO.class))).thenReturn(mockPackage);

        mockMvc.perform(post("/paquetes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(packageTravelJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Glaciar Perito Moreno: Hielo Milenario"));
    }

    @Test
    void testUpdate_Success() throws Exception {
        when(iPackageTravelService.update(eq(1L), any(PackageTravelRequestDTO.class))).thenReturn(mockPackage);

        mockMvc.perform(put("/paquetes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(packageTravelJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testUpdate_Error() throws Exception {
        when(iPackageTravelService.update(eq(1L), any(PackageTravelRequestDTO.class)))
                .thenThrow(new RuntimeException("Error interno"));

        mockMvc.perform(put("/paquetes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(packageTravelJson))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString("Error al actualizar: Error interno")));
    }


    @Test
    void testDeletePackageTravel() throws Exception {
        doNothing().when(iPackageTravelService).delete(1L);

        mockMvc.perform(delete("/paquetes/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Se eliminó de forma correcta el paquete de viaje con el Id: 1"));

        verify(iPackageTravelService, times(1)).delete(1L);
    }
}