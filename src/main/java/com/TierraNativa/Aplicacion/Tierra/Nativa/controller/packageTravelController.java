package com.tierranativa.aplicacion.tierra.nativa.controller;

import com.tierranativa.aplicacion.tierra.nativa.entity.*;
import com.tierranativa.aplicacion.tierra.nativa.exception.resourceNotFoundException;
import com.tierranativa.aplicacion.tierra.nativa.service.implement.iPackageTravelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/paquetes")
@CrossOrigin(origins = "http://localhost:5173")
public class packageTravelController {

    iPackageTravelService iPackageTravelService;

    @Autowired
    public packageTravelController(iPackageTravelService iPackageTravelService) {
        this.iPackageTravelService = iPackageTravelService;
    }

    @PostMapping
        public ResponseEntity<packageTravel> registerPackage(@Validated @RequestBody packageTravelRequestDTO packageDTO)  {
            packageTravel createdPackage = iPackageTravelService.registerNewPackage(packageDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdPackage);
        }

    @GetMapping("/admin")
    public ResponseEntity<List<packageTravel>> getAllPackagesForAdmin() {
        List<packageTravel> packages = iPackageTravelService.findAll();
        return ResponseEntity.ok(packages);
    }

    @PutMapping
    public ResponseEntity<packageTravel> update(@RequestBody packageTravel packageTravel) {
        try {
            packageTravel updatedPackage = iPackageTravelService.update(packageTravel);
            return ResponseEntity.ok(updatedPackage);
        } catch (resourceNotFoundException e) {
            throw e;
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<packageTravel> findById(@PathVariable Long id) {
        Optional<packageTravel> packageTravel = iPackageTravelService.findById(id);
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
    public ResponseEntity<List<packageTravel>> findByCategory(@PathVariable packageCategory category) {
        List<packageTravel> packageTravels = iPackageTravelService.findByCategory(category);
        if (packageTravels.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(packageTravels);
        }
    }
}