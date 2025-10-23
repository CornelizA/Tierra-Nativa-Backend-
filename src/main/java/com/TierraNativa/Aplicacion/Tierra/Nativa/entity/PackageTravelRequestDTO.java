package com.TierraNativa.Aplicacion.Tierra.Nativa.entity;

import lombok.Data;

import java.util.List;

@Data
public class PackageTravelRequestDTO {

    private String name;
    private ItineraryDetailDTO itineraryDetail;
    private Double basePrice;
    private List<ImageDTO> imageDetails;
    private String shortDescription;
    private String destination;
    private String category;
}

