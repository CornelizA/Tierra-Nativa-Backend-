package com.tierranativa.aplicacion.tierra.nativa.controller;

import com.tierranativa.aplicacion.tierra.nativa.dto.PackageTravelRequestDTO;
import com.tierranativa.aplicacion.tierra.nativa.entity.*;
import com.tierranativa.aplicacion.tierra.nativa.service.implement.IPackageTravelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public ResponseEntity<PackageTravelRequestDTO> registerPackage(@Validated @RequestBody PackageTravelRequestDTO packageDTO) {
        PackageTravel createdPackage = iPackageTravelService.registerNewPackage(packageDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(PackageTravelRequestDTO.fromEntity(createdPackage));
    }

    @GetMapping
    public ResponseEntity<List<PackageTravelRequestDTO>> getAllPackagesPublic() {
        List<PackageTravelRequestDTO> packages = iPackageTravelService.findAll()
                .stream()
                .map(PackageTravelRequestDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(packages);
    }

    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<PackageTravelRequestDTO>> getAllPackagesForAdmin() {
        List<PackageTravelRequestDTO> packages = iPackageTravelService.findAll()
                .stream()
                .map(PackageTravelRequestDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(packages);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody PackageTravelRequestDTO updateDto) {
        try {
            PackageTravel updatedPackage = iPackageTravelService.update(id, updateDto);
            return ResponseEntity.ok(PackageTravelRequestDTO.fromEntity(updatedPackage));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al actualizar: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<PackageTravelRequestDTO> findById(@PathVariable Long id) {
        Optional<PackageTravel> packageTravel = iPackageTravelService.findById(id);
        return packageTravel
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