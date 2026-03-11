package com.tierranativa.aplicacion.tierra.nativa.controller;

import com.tierranativa.aplicacion.tierra.nativa.dto.CategoryPackagesDTO;
import com.tierranativa.aplicacion.tierra.nativa.entity.Category;
import com.tierranativa.aplicacion.tierra.nativa.service.implement.ICategoryService;
import com.tierranativa.aplicacion.tierra.nativa.service.implement.IPackageTravelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private ICategoryService iCategoryService;
    private IPackageTravelService iPackageTravelService;

    @Autowired
    public CategoryController(ICategoryService iCategoryService, IPackageTravelService iPackageTravelService) {
        this.iCategoryService = iCategoryService;
        this.iPackageTravelService = iPackageTravelService;
    }

    @GetMapping
    public ResponseEntity<List<Category>> findAllCategories() {
        return ResponseEntity.ok(iCategoryService.findAll());
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Category> createCategory(@RequestBody Category category) {
        return ResponseEntity.status(HttpStatus.CREATED).body(iCategoryService.save(category));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Category> update(@PathVariable Long id, @RequestBody Category category) {
        category.setId(id);
        return ResponseEntity.ok(iCategoryService.update(category));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> deleteByID(@PathVariable Long id) {
        iCategoryService.deleteById(id);
        return ResponseEntity.ok("Se eliminó de forma correcta la categoría con el Id: " + id);
    }

    @GetMapping("/categoria/{categoryTitle}")
    public ResponseEntity<CategoryPackagesDTO> findByCategory(@PathVariable String categoryTitle) {
        return ResponseEntity.ok(iPackageTravelService.getCategoryPackagesDTO(categoryTitle));
    }
}