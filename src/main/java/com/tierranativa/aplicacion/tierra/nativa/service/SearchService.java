package com.tierranativa.aplicacion.tierra.nativa.service;

import com.tierranativa.aplicacion.tierra.nativa.dto.PackageTravelRequestDTO;
import com.tierranativa.aplicacion.tierra.nativa.dto.SearchRequestDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SearchService {
    List<PackageTravelRequestDTO> search(SearchRequestDTO searchRequest);
}