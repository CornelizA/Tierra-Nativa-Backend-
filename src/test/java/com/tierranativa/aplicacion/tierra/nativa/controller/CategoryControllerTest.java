package com.tierranativa.aplicacion.tierra.nativa.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tierranativa.aplicacion.tierra.nativa.dto.CategoryPackagesDTO;
import com.tierranativa.aplicacion.tierra.nativa.entity.Category;
import com.tierranativa.aplicacion.tierra.nativa.entity.PackageTravel;
import com.tierranativa.aplicacion.tierra.nativa.service.implement.ICategoryService;
import com.tierranativa.aplicacion.tierra.nativa.service.implement.IPackageTravelService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ICategoryService categoryService;

    @MockitoBean
    private IPackageTravelService packageService;

    private Category mockCategory;

    @BeforeEach
    void setUp() {
        mockCategory = Category.builder()
                .id(1L)
                .title("AVENTURA")
                .description("Descripción de prueba")
                .imageUrl("http://img.url")
                .build();
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    void createCategory_Success() throws Exception {
        when(categoryService.save(any(Category.class))).thenReturn(mockCategory);

        mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockCategory)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("AVENTURA"));
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    void deleteByID_Success() throws Exception {
        Mockito.doNothing().when(categoryService).deleteById(1L);

        mockMvc.perform(delete("/categories/1"))
                .andExpect(status().isOk());
    }

    @Test
    void findByCategory_Success() throws Exception {
        String title = "AVENTURA";

        PackageTravel pkg = new PackageTravel();
        pkg.setId(10L);
        pkg.setName("Paquete Relacionado");
        pkg.setImages(new ArrayList<>());
        pkg.setCategories(new java.util.HashSet<>(List.of(mockCategory)));

        CategoryPackagesDTO mockDto = CategoryPackagesDTO.from(mockCategory, List.of(pkg));

        when(packageService.getCategoryPackagesDTO(title)).thenReturn(mockDto);

        mockMvc.perform(get("/categories/categoria/{categoryTitle}", title)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoryDetails.title").value("AVENTURA"))
                .andExpect(jsonPath("$.packages[0].name").value("Paquete Relacionado"));
    }
}