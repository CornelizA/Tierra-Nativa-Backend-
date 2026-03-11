package com.tierranativa.aplicacion.tierra.nativa.controller;

import com.tierranativa.aplicacion.tierra.nativa.dto.ImageDTO;
import com.tierranativa.aplicacion.tierra.nativa.dto.ItineraryDetailDTO;
import com.tierranativa.aplicacion.tierra.nativa.dto.PackageTravelRequestDTO;
import com.tierranativa.aplicacion.tierra.nativa.entity.*;
import com.tierranativa.aplicacion.tierra.nativa.service.implement.IPackageTravelService;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.containsString;

@SpringBootTest
@AutoConfigureMockMvc
class PackageTravelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private IPackageTravelService iPackageTravelService;

    private PackageTravel mockPackage;
    private String packageTravelJson;

    @BeforeEach
    void setUp() {
        Category geoPaisajes = Category.builder()
                .id(1L)
                .title("GEOPAISAJES")
                .build();

        PackageItineraryDetail detail = PackageItineraryDetail.builder()
                .duration("4 Días")
                .lodgingType("Hotel")
                .build();

        mockPackage = new PackageTravel();
        mockPackage.setId(1L);
        mockPackage.setName("Glaciar Perito Moreno: Hielo Milenario");
        mockPackage.setBasePrice(690000.00);
        mockPackage.setDestination("El Calafate");
        mockPackage.setCategories(Set.of(geoPaisajes));
        mockPackage.setCharacteristics(new java.util.HashSet<>());
        mockPackage.setImages(new ArrayList<>());
        mockPackage.setBookings(new ArrayList<>());
        mockPackage.setReviews(new ArrayList<>());
        mockPackage.setItineraryDetail(detail);

        PackageTravelRequestDTO validDto = PackageTravelRequestDTO.builder()
                .name("Glaciar Perito Moreno: Hielo Milenario")
                .basePrice(690000.00)
                .shortDescription("Descripción válida de más de diez caracteres")
                .destination("El Calafate")
                .categoryId(Set.of(1L))
                .itineraryDetail(new ItineraryDetailDTO())
                .imageDetails(List.of(new ImageDTO()))
                .build();

        try {
            packageTravelJson = objectMapper.writeValueAsString(validDto);
        } catch (Exception e) {
            throw new RuntimeException("Error al preparar JSON", e);
        }
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    void testRegisterPackage() throws Exception {
        Mockito.when(iPackageTravelService.registerNewPackage(any(), any()))
                .thenReturn(mockPackage);

        mockMvc.perform(post("/paquetes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(packageTravelJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Glaciar Perito Moreno: Hielo Milenario"));
    }

    @Test
    void testGetAllPackagesPublic() throws Exception {
        Mockito.when(iPackageTravelService.findAll()).thenReturn(List.of(mockPackage));

        mockMvc.perform(get("/paquetes")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Glaciar Perito Moreno: Hielo Milenario"));
    }

    @Test
    void testFindById_Success() throws Exception {
        Mockito.when(iPackageTravelService.findById(1L)).thenReturn(Optional.of(mockPackage));

        mockMvc.perform(get("/paquetes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testFindById_NotFound() throws Exception {
        Mockito.when(iPackageTravelService.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/paquetes/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    void testUpdate_Success() throws Exception {
        Mockito.when(iPackageTravelService.update(any(), any(), any()))
                .thenReturn(mockPackage);

        mockMvc.perform(put("/paquetes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(packageTravelJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    void testDeletePackageTravel() throws Exception {
        Mockito.doNothing().when(iPackageTravelService).delete(1L);

        mockMvc.perform(delete("/paquetes/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Se eliminó de forma correcta")));
    }
}