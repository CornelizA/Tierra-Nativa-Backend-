package com.TierraNativa.Aplicacion.Tierra.Nativa.controller;

import com.TierraNativa.Aplicacion.Tierra.Nativa.entity.*;
import com.TierraNativa.Aplicacion.Tierra.Nativa.exception.ResourceNotFoundException;
import com.TierraNativa.Aplicacion.Tierra.Nativa.service.implement.IPackageTravelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/paquetes")
@CrossOrigin(origins = "http://localhost:5173")
public class PackageTravelController {

    IPackageTravelService iPackageTravelService;

    @Autowired
    public PackageTravelController(IPackageTravelService iPackageTravelService) {
        this.iPackageTravelService = iPackageTravelService;
    }

    @PostMapping
    public ResponseEntity<?> registerPackage(@Validated @RequestBody PackageTravelRequestDTO packageDTO, BindingResult result) {
        if (result.hasErrors()) {
            result.getAllErrors().forEach(error -> {
                System.err.println("Validation Error: " + error.getDefaultMessage());
                System.err.println("Field: " + error.getObjectName());
            });

            return new ResponseEntity<>("Validation failed: see server logs", HttpStatus.BAD_REQUEST);
        }

        try {
            PackageTravel createdPackage = iPackageTravelService.registerNewPackage(packageDTO);
            return new ResponseEntity<>(createdPackage, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/admin")
    public ResponseEntity<List<PackageTravel>> getAllPackagesForAdmin() {
        List<PackageTravel> packages = iPackageTravelService.findAll();
        return ResponseEntity.ok(packages);
    }

    @GetMapping
    public ResponseEntity<List<PackageTravel>> findAll() {
        List<PackageTravel> packages = iPackageTravelService.findAll();
        return ResponseEntity.ok(packages);
    }

    @PutMapping
    public ResponseEntity<String> update(@RequestBody PackageTravel packageTravel) {
        try {
            iPackageTravelService.update(packageTravel);
            return ResponseEntity.ok("El paquete de viaje ha sido actualizado de forma exitosa.");
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
    public ResponseEntity<String> delete(@PathVariable Long id) {
        iPackageTravelService.delete(id);
        return ResponseEntity.ok("Se eliminó de forma correcta el paquete de viaje con el Id: " + id);
    }

    @GetMapping("/categoria/{category}")
    public ResponseEntity<List<PackageTravel>> findByCategory(@PathVariable PackageCategory category) {
        List<PackageTravel> packageTravels = iPackageTravelService.findByCategory(category);
        if (packageTravels.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(packageTravels);
        }
    }
}