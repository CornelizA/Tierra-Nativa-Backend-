package com.tierranativa.aplicacion.tierra.nativa.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewRequestDTO {

    private Integer score;

    private String comment;

    private Long packageId;

}
