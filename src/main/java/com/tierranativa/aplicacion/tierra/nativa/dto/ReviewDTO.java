package com.tierranativa.aplicacion.tierra.nativa.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewDTO {

    private Long id;
    private Integer score;
    private String comment;
    private String userName;
    private LocalDateTime date;
}
