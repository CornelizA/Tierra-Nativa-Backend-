package com.tierranativa.aplicacion.tierra.nativa.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tierranativa.aplicacion.tierra.nativa.entity.Category;
import com.tierranativa.aplicacion.tierra.nativa.entity.PackageTravel;
import com.tierranativa.aplicacion.tierra.nativa.service.implement.ICategoryService;
import com.tierranativa.aplicacion.tierra.nativa.service.implement.IPackageTravelService;
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
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CategoryControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ICategoryService iCategoryService;

    @Mock
    private IPackageTravelService iPackageTravelService;

    @InjectMocks
    private CategoryController categoryController;

    private ObjectMapper objectMapper = new ObjectMapper();
    private Category mockCategory;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(categoryController).build();
        mockCategory = Category.builder()
                .id(1L)
                .title("AVENTURA")
                .description("Descripción de prueba")
                .build();
    }

    @Test
    void createCategory_Success() throws Exception {
        when(iCategoryService.save(any(Category.class))).thenReturn(mockCategory);

        mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockCategory)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("AVENTURA"));
    }

    @Test
    void deleteByID_Success() throws Exception {
        doNothing().when(iCategoryService).deleteById(1L);

        mockMvc.perform(delete("/categories/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Se eliminó de forma correcta la categoría con el Id: 1"));
    }

    @Test
    void findByCategory_Success() throws Exception {

        String title = "AVENTURA";
        PackageTravel pkg = new PackageTravel();
        pkg.setName("Paquete Relacionado");

        when(iPackageTravelService.findCategoryByTitle(title)).thenReturn(Optional.of(mockCategory));
        when(iPackageTravelService.findByCategoryTitle(title)).thenReturn(List.of(pkg));

        mockMvc.perform(get("/categories/categoria/{categoryTitle}", title))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoryDetails.title").value(title))
                .andExpect(jsonPath("$.packages").isArray());
    }
}