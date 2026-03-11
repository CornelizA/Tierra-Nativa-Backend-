package com.tierranativa.aplicacion.tierra.nativa.service.implement;

import com.tierranativa.aplicacion.tierra.nativa.dto.PackageTravelRequestDTO;
import com.tierranativa.aplicacion.tierra.nativa.dto.SearchRequestDTO;
import com.tierranativa.aplicacion.tierra.nativa.entity.PackageTravel;
import com.tierranativa.aplicacion.tierra.nativa.repository.SearchRepository;
import com.tierranativa.aplicacion.tierra.nativa.service.SearchService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ISearchService implements SearchService {

    private final SearchRepository searchRepository;

    @Override
    public List<PackageTravelRequestDTO> search(SearchRequestDTO searchRequest) {
        List<PackageTravel> results;

        if (searchRequest.getCheckIn() != null && searchRequest.getCheckOut() != null) {
            results = searchRepository.findAvailablePackages(
                    searchRequest.getKeyword() != null ? searchRequest.getKeyword() : "",
                    searchRequest.getCheckIn(),
                    searchRequest.getCheckOut()
            );
        } else {
            results = searchRepository.findByKeywordOnly(
                    searchRequest.getKeyword() != null ? searchRequest.getKeyword() : ""
            );
        }
        return results.stream()
                .map(PackageTravelRequestDTO::fromEntity)
                .collect(Collectors.toList());
    }
}