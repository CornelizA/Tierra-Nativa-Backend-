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
public class PackageTravel {

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
    private PackageCategory category;

    private String imageUrl;

    @JsonManagedReference
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToOne(mappedBy = "packageTravel", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private PackageItineraryDetail itineraryDetail;

    @JsonManagedReference
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "packageTravel", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<PackageImage> images = new ArrayList<>();

    public void addImage(PackageImage image) {
        if (image == null || images.contains(image)) return;
        images.add(image);
        if (image.getPackageTravel() != this) {
            image.setPackageTravel(this);
        }
    }

    public void removeImage(PackageImage image) {
        if (image == null || !images.contains(image)) return;
        images.remove(image);
        if (image.getPackageTravel() == this) {
            image.setPackageTravel(null);
        }
    }

    public void setItineraryDetail(PackageItineraryDetail detail) {
        if (this.itineraryDetail == detail) return;

        PackageItineraryDetail previous = this.itineraryDetail;
        this.itineraryDetail = detail;

        if (previous != null && previous.getPackageTravel() == this) {
            previous.setPackageTravel(null);
        }
        if (detail != null && detail.getPackageTravel() != this) {
            detail.setPackageTravel(this);
        }
    }

    public void syncImages(List<PackageImage> newImages) {
        this.images.clear();
        if (newImages != null) {
            for (PackageImage image : newImages) {
                this.addImage(image);
            }
        }
    }
}
