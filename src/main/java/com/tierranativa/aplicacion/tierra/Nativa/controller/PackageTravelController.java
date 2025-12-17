package com.tierranativa.aplicacion.tierra.nativa.controller;

import com.tierranativa.aplicacion.tierra.nativa.dto.CategoryPackagesDTO;
import com.tierranativa.aplicacion.tierra.nativa.dto.PackageTravelRequestDTO;
import com.tierranativa.aplicacion.tierra.nativa.entity.*;
import com.tierranativa.aplicacion.tierra.nativa.exception.ResourceNotFoundException;
import com.tierranativa.aplicacion.tierra.nativa.service.implement.IPackageTravelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/paquetes")
public class PackageTravelController {

    IPackageTravelService iPackageTravelService;

    @Autowired
    public PackageTravelController(IPackageTravelService iPackageTravelService) {
        this.iPackageTravelService = iPackageTravelService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<PackageTravel> registerPackage(@Validated @RequestBody PackageTravelRequestDTO packageDTO) {
        PackageTravel createdPackage = iPackageTravelService.registerNewPackage(packageDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPackage);
    }


    @GetMapping
    public ResponseEntity<List<PackageTravel>> getAllPackagesPublic() {
        List<PackageTravel> packages = iPackageTravelService.findAll();
        return ResponseEntity.ok(packages);
    }

    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<PackageTravel>> getAllPackagesForAdmin() {
        List<PackageTravel> packages = iPackageTravelService.findAll();
        return ResponseEntity.ok(packages);
    }


    @PutMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<PackageTravel> update(@RequestBody PackageTravel packageTravel) {
        try {
            PackageTravel updatedPackage = iPackageTravelService.update(packageTravel);
            return ResponseEntity.ok(updatedPackage);
        } catch (ResourceNotFoundException e) {
            throw e;
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<PackageTravel> findById(@PathVariable Long id) {
        Optional<PackageTravel> packageTravel = iPackageTravelService.findById(id);
        if (packageTravel.isPresent()) {
            return ResponseEntity.ok(packageTravel.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        iPackageTravelService.delete(id);
        return ResponseEntity.ok("Se eliminó de forma correcta el paquete de viaje con el Id: " + id);
    }

}