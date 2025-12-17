package com.tierranativa.aplicacion.tierra.nativa.service;

import com.tierranativa.aplicacion.tierra.nativa.entity.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {

    Category save(Category category);

    Optional<Category> findById(Long id);

    Category update(Category category);

    List<Category> findAll();

    void deleteById(Long id);
}
