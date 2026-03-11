package com.tierranativa.aplicacion.tierra.nativa.controller;

import com.tierranativa.aplicacion.tierra.nativa.dto.PackageTravelRequestDTO;
import com.tierranativa.aplicacion.tierra.nativa.entity.PackageTravel;
import com.tierranativa.aplicacion.tierra.nativa.entity.User;
import com.tierranativa.aplicacion.tierra.nativa.service.implement.IPackageTravelService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/paquetes")
public class PackageTravelController {

    private final IPackageTravelService iPackageTravelService;

    @Autowired
    public PackageTravelController(IPackageTravelService iPackageTravelService) {
        this.iPackageTravelService = iPackageTravelService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<PackageTravelRequestDTO> registerPackage(@Validated @RequestBody PackageTravelRequestDTO packageDTO, @AuthenticationPrincipal User admin) throws Exception {
        PackageTravel createdPackage = iPackageTravelService.registerNewPackage(packageDTO, admin);
        return ResponseEntity.status(HttpStatus.CREATED).body(PackageTravelRequestDTO.fromEntity(createdPackage));
    }

    @GetMapping
    public ResponseEntity<List<PackageTravelRequestDTO>> getAllPackages() {
        return ResponseEntity.ok(iPackageTravelService.findAll().stream()
                .map(PackageTravelRequestDTO::fromEntity)
                .toList());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<PackageTravelRequestDTO> update(@PathVariable Long id, @Valid @RequestBody PackageTravelRequestDTO updateDto, @AuthenticationPrincipal User admin) throws Exception {
        PackageTravel updatedPackage = iPackageTravelService.update(id, updateDto, admin);
        return ResponseEntity.ok(PackageTravelRequestDTO.fromEntity(updatedPackage));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PackageTravelRequestDTO> findById(@PathVariable Long id) {
        return iPackageTravelService.findById(id)
                .map(entity -> ResponseEntity.ok(PackageTravelRequestDTO.fromEntity(entity)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        iPackageTravelService.delete(id);
        return ResponseEntity.ok("Se eliminó de forma correcta el paquete de viaje con el Id: " + id);
    }
}