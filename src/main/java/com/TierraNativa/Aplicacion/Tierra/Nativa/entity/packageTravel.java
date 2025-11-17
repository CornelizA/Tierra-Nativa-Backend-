package com.tierranativa.aplicacion.tierra.nativa.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "package_travel")
public class packageTravel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "base_price", nullable = false)
    private Double basePrice;

    @Column(name = "short_description", nullable = false)
    private String shortDescription;

    @Column(name = "destination", nullable = false)
    private String destination;

    @Enumerated(EnumType.STRING)
    private packageCategory category;

    private String imageUrl;

    @JsonManagedReference
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToOne(mappedBy = "packageTravel", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private packageItineraryDetail itineraryDetail;

    @JsonManagedReference
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "packageTravel", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<packageImage> images = new ArrayList<>();

    public void addImage(packageImage image) {
        if (image == null || images.contains(image)) return;
        images.add(image);
        if (image.getPackageTravel() != this) {
            image.setPackageTravel(this);
        }
    }

    public void removeImage(packageImage image) {
        if (image == null || !images.contains(image)) return;
        images.remove(image);
        if (image.getPackageTravel() == this) {
            image.setPackageTravel(null);
        }
    }

    public void setItineraryDetail(packageItineraryDetail detail) {
        if (this.itineraryDetail == detail) return;

        packageItineraryDetail previous = this.itineraryDetail;
        this.itineraryDetail = detail;

        if (previous != null && previous.getPackageTravel() == this) {
            previous.setPackageTravel(null);
        }
        if (detail != null && detail.getPackageTravel() != this) {
            detail.setPackageTravel(this);
        }
    }
}
