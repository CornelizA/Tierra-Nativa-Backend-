package com.tierranativa.aplicacion.tierra.nativa.service;

import com.tierranativa.aplicacion.tierra.nativa.entity.Category;
import com.tierranativa.aplicacion.tierra.nativa.repository.CategoryRepository;
import com.tierranativa.aplicacion.tierra.nativa.service.implement.ICategoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ICategoryService categoryService;

    @Test
    void saveCategory_Success() {
        Category category = Category.builder().title("NUEVA").build();
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        Category saved = categoryService.save(category);

        assertThat(saved.getTitle()).isEqualTo("NUEVA");
        verify(categoryRepository, times(1)).save(category);
    }

    @Test
    void findById_Success() {
        Category category = Category.builder().id(1L).title("TEST").build();
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        Optional<Category> found = categoryService.findById(1L);

        assertThat(found).isPresent();
        assertThat(found.get().getTitle()).isEqualTo("TEST");
    }
}