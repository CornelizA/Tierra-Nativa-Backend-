package com.tierranativa.aplicacion.tierra.nativa.repository;

import com.tierranativa.aplicacion.tierra.nativa.entity.packageCategory;
import com.tierranativa.aplicacion.tierra.nativa.entity.packageImage;
import com.tierranativa.aplicacion.tierra.nativa.entity.packageItineraryDetail;
import com.tierranativa.aplicacion.tierra.nativa.entity.packageTravel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class packageTravelRepositoryTest {

    @Autowired
    private packageTravelRepository packageTravelRepository;
    private packageTravel packageTravel1;
    private packageTravel packageTravel2;

    @BeforeEach
    void setUp() {
        packageItineraryDetail itineraryDetail1 = new packageItineraryDetail();
        itineraryDetail1.setDuration("7 Días");
        itineraryDetail1.setLodgingType("Hotel");
        itineraryDetail1.setTransferType("Vuelos-Terrestres");
        itineraryDetail1.setDailyActivitiesDescription("Actividades diarias");
        itineraryDetail1.setFoodAndHydrationNotes("Alimentación variada");
        itineraryDetail1.setGeneralRecommendations("Diviertanse");

        packageItineraryDetail itineraryDetail2 = new packageItineraryDetail();
        itineraryDetail2.setDuration("3 Días");
        itineraryDetail2.setLodgingType("Cabaña");
        itineraryDetail2.setTransferType("Terrestre");
        itineraryDetail2.setDailyActivitiesDescription("Actividades 2");
        itineraryDetail2.setFoodAndHydrationNotes("Notas 2");
        itineraryDetail2.setGeneralRecommendations("Recomendaciones 2");

        packageImage image1 = new packageImage();
        image1.setUrl("https://ruta/imagen_patagonia_principal.jpg");
        image1.setPrincipal(true);

        packageImage image2 = new packageImage();
        image2.setUrl("https://ruta/imagen_cordoba_principal.jpg");
        image2.setPrincipal(true);

        packageTravel1 = new packageTravel();
        packageTravel1.setName("Aventura Patagonia");
        packageTravel1.setBasePrice(1500.00);
        packageTravel1.setDestination("Patagonia Argentina");
        packageTravel1.setShortDescription("Descripción corta aventura.");
        packageTravel1.setCategory(packageCategory.GEOPAISAJES);
        packageTravel1.setItineraryDetail(itineraryDetail1);
        itineraryDetail1.setPackageTravel(packageTravel1);
        image1.setPackageTravel(packageTravel1);
        packageTravel1.setImages(List.of(image1));
        packageTravel1 = packageTravelRepository.save(packageTravel1);

        packageTravel2 = new packageTravel();
        packageTravel2.setName("Relajación Cordobesa");
        packageTravel2.setBasePrice(800.50);
        packageTravel2.setDestination("Sierras de Córdoba");
        packageTravel2.setShortDescription("Descripción corta relajación.");
        packageTravel2.setCategory(packageCategory.RELAJACION);
        packageTravel2.setItineraryDetail(itineraryDetail2);
        itineraryDetail2.setPackageTravel(packageTravel2);
        image2.setPackageTravel(packageTravel2);
        packageTravel2.setImages(List.of(image2));
        packageTravelRepository.save(packageTravel2);
    }

    @Test
    void findByCategory() {
        List<packageTravel> foundPackages = packageTravelRepository.findByCategory(packageCategory.GEOPAISAJES);
        assertThat(foundPackages).hasSize(1);
        assertThat(foundPackages.get(0).getName()).isEqualTo("Aventura Patagonia");
        assertThat(foundPackages.get(0).getImages()).isNotEmpty();
        assertThat(foundPackages.get(0).getImages().get(0).getUrl()).contains("patagonia_principal");
    }

    @Test
    void existsByName() {

        boolean exists = packageTravelRepository.existsByName("Aventura Patagonia");
        assertThat(exists).isTrue();
    }

    @Test
    void existsByNameAndIdNot() {

        Long differentId = packageTravel2.getId();
        boolean existsWithDifferentId = packageTravelRepository.existsByNameAndIdNot(
                packageTravel1.getName(),
                differentId
        );
        assertThat(existsWithDifferentId).isTrue();
        boolean existsForSameId = packageTravelRepository.existsByNameAndIdNot(
                packageTravel1.getName(),
                packageTravel1.getId()
        );
        assertThat(existsForSameId).isFalse();
    }
}