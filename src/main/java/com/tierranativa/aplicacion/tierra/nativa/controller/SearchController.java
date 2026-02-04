package com.tierranativa.aplicacion.tierra.nativa.controller;

import com.tierranativa.aplicacion.tierra.nativa.dto.PackageTravelRequestDTO;
import com.tierranativa.aplicacion.tierra.nativa.dto.SearchRequestDTO;
import com.tierranativa.aplicacion.tierra.nativa.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @GetMapping
    public ResponseEntity<List<PackageTravelRequestDTO>> getAvailablePackages(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) LocalDate checkIn,
            @RequestParam(required = false) LocalDate checkOut) {

        SearchRequestDTO request = SearchRequestDTO.builder()
                .keyword(keyword)
                .checkIn(checkIn)
                .checkOut(checkOut)
                .build();

        return ResponseEntity.ok(searchService.search(request));
    }
}