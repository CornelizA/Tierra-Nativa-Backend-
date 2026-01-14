package com.tierranativa.aplicacion.tierra.nativa.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "package_characteristics")
public class PackageCharacteristics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Long id;

    @Column(nullable = false)
     private String title;

    @Column(nullable = false)
    private String icon;

    @ManyToMany(mappedBy = "characteristics")
    @Builder.Default
    @JsonIgnoreProperties("characteristics")
    private Set<PackageTravel> packages = new HashSet<>();
}

