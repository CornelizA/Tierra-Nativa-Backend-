package com.tierranativa.aplicacion.tierra.nativa.controller;

import com.tierranativa.aplicacion.tierra.nativa.dto.CharacteristicDTO;
import com.tierranativa.aplicacion.tierra.nativa.entity.PackageCharacteristics;
import com.tierranativa.aplicacion.tierra.nativa.exception.ResourceNotFoundException;
import com.tierranativa.aplicacion.tierra.nativa.service.implement.ICharacteristicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/characteristics")

public class CharacteristicController {

    private ICharacteristicService iCharacteristicService;

    @Autowired
    public CharacteristicController(ICharacteristicService iCharacteristicService) {
        this.iCharacteristicService = iCharacteristicService;
    }

    @GetMapping("/public")
    public ResponseEntity<List<CharacteristicDTO>> findAllCharacteristicPublic() {
        return ResponseEntity.ok(iCharacteristicService.findAll());
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<CharacteristicDTO>> findAllCharacteristics() {
        return ResponseEntity.ok(iCharacteristicService.findAll());
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<CharacteristicDTO> createCharacteristics(@RequestBody PackageCharacteristics packageCharacteristics) {
        try {
            CharacteristicDTO savedCharacteristics = iCharacteristicService.save(packageCharacteristics);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedCharacteristics);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PutMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<CharacteristicDTO> update(@RequestBody PackageCharacteristics packageCharacteristics) {
        try {
            CharacteristicDTO updatedCharacteristics = iCharacteristicService.update(packageCharacteristics);
            return ResponseEntity.ok(updatedCharacteristics);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> deleteByID(@PathVariable Long id) {
        try {
            iCharacteristicService.deleteById(id);
            return ResponseEntity.ok("Se eliminó de forma correcta la característica con el Id: " + id);
        } catch (IllegalArgumentException | ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró la característica con ID: " + id);
        }
    }
}
