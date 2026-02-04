package com.tierranativa.aplicacion.tierra.nativa.controller;

import com.tierranativa.aplicacion.tierra.nativa.dto.CharacteristicDTO;
import com.tierranativa.aplicacion.tierra.nativa.entity.PackageCharacteristics;
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

    @GetMapping
    public ResponseEntity<List<CharacteristicDTO>> findAllCharacteristic() {
        return ResponseEntity.ok(iCharacteristicService.findAll());
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<CharacteristicDTO> createCharacteristics(@RequestBody PackageCharacteristics characteristics) {
        return ResponseEntity.status(HttpStatus.CREATED).body(iCharacteristicService.save(characteristics));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<CharacteristicDTO> update(@PathVariable Long id, @RequestBody PackageCharacteristics packageCharacteristics) {
        packageCharacteristics.setId(id);
        return ResponseEntity.ok(iCharacteristicService.update(packageCharacteristics));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> deleteByID(@PathVariable Long id) {
        iCharacteristicService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
