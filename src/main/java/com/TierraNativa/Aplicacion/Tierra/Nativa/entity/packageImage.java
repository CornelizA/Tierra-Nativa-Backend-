package com.tierranativa.aplicacion.tierra.nativa.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "package_image")
public class packageImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "package_image_id")
    private Long id;

    @Column(nullable = false)
    private String url;

    private Boolean principal;

    @JsonBackReference
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "package_travel_id")
    private packageTravel packageTravel;

    public void setPackageTravel(packageTravel packageTravel) {
        if (this.packageTravel == packageTravel) return;

        packageTravel previous = this.packageTravel;
        this.packageTravel = packageTravel;

        if (previous != null && previous.getImages().contains(this)) {
            previous.getImages().remove(this);
        }
        if (packageTravel != null && !packageTravel.getImages().contains(this)) {
            packageTravel.getImages().add(this);
        }
    }
}
