package com.TierraNativa.Aplicacion.Tierra.Nativa.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PackageTravelRequestDTO {

    private String name;
    private ItineraryDetailDTO itineraryDetail;
    private Double basePrice;
    private List<ImageDTO> imageDetails;
    private String shortDescription;
    private String destination;
    private String category;
}

