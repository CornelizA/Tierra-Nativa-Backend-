package com.TierraNativa.Aplicacion.Tierra.Nativa.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "package_itinerary_detail")

public class PackageItineraryDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "duration", nullable = false)
    private String duration;

    @Column(name = "lodging_type", nullable = false)
    private String lodgingType;

    @Column(name = "transfer_type", nullable = false)
    private String transferType;

    @Lob
    @Column(name = "daily_activities_description", nullable = false)
    private String dailyActivitiesDescription;

    @Lob
    @Column(name = "food_and_hydration_notes", nullable = false)
    private String foodAndHydrationNotes;

    @Lob
    @Column(name = "general_recommendations")
    private String generalRecommendations;


    @JsonBackReference
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "package_travel_id")
    private PackageTravel packageTravel;

    public void setPackageTravel(PackageTravel packageTravel) {
        if (this.packageTravel == packageTravel) return;

        PackageTravel previous = this.packageTravel;
        this.packageTravel = packageTravel;

        if (previous != null && previous.getItineraryDetail() == this) {
            previous.setItineraryDetail(null);
        }
        if (packageTravel != null && packageTravel.getItineraryDetail() != this) {
            packageTravel.setItineraryDetail(this);
        }
    }
}




