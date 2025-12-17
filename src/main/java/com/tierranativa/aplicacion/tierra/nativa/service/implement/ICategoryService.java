package com.tierranativa.aplicacion.tierra.nativa.service.implement;

import com.tierranativa.aplicacion.tierra.nativa.entity.Category;
import com.tierranativa.aplicacion.tierra.nativa.repository.CategoryRepository;
import com.tierranativa.aplicacion.tierra.nativa.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ICategoryService implements CategoryService {

    private CategoryRepository categoryRepository;

    @Autowired
    public ICategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }


    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public Category save(Category category) {
        if (categoryRepository.findByTitle(category.getTitle()).isPresent()) {
            throw new IllegalArgumentException("Ya existe una categoría con ese título: " + category.getTitle());
        }
        return categoryRepository.save(category);
    }

    @Override
    public Optional<Category> findById(Long id) {
        return categoryRepository.findById(id);
    }

    @Override
    public Category update(Category category) {
        Optional<Category> existingCategory = categoryRepository.findById(category.getId());
        if (existingCategory.isEmpty()) {
            throw new IllegalArgumentException("La categoría con ID " + category.getId() + " no fue encontrada para actualizar.");
        }
        Optional<Category> categoryWithSameTitle = categoryRepository.findByTitle(category.getTitle());

        if (categoryWithSameTitle.isPresent() && !categoryWithSameTitle.get().getId().equals(category.getId())) {
            throw new IllegalArgumentException("El título '" + category.getTitle() + "' ya está en uso por otra categoría.");
        }
        return categoryRepository.save(category);
    }

    @Override
    public void deleteById(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new IllegalArgumentException("La categoría con ID " + id + " no existe.");
        }
        categoryRepository.deleteById(id);
    }
}
