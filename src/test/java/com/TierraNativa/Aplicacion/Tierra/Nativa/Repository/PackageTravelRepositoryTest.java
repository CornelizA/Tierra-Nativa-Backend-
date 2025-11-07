package com.TierraNativa.Aplicacion.Tierra.Nativa.Repository;

import com.TierraNativa.Aplicacion.Tierra.Nativa.entity.PackageCategory;
import com.TierraNativa.Aplicacion.Tierra.Nativa.entity.PackageImage;
import com.TierraNativa.Aplicacion.Tierra.Nativa.entity.PackageItineraryDetail;
import com.TierraNativa.Aplicacion.Tierra.Nativa.entity.PackageTravel;
import com.TierraNativa.Aplicacion.Tierra.Nativa.repository.PackageTravelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class PackageTravelRepositoryTest {

    @Autowired
    private PackageTravelRepository packageTravelRepository;
    private PackageTravel packageTravel1;
    private PackageTravel packageTravel2;

    @BeforeEach
    void setUp() {
        PackageItineraryDetail itineraryDetail1 = new PackageItineraryDetail();
        itineraryDetail1.setDuration("7 Días");
        itineraryDetail1.setLodgingType("Hotel");
        itineraryDetail1.setTransferType("Vuelos-Terrestres");
        itineraryDetail1.setDailyActivitiesDescription("Actividades diarias");
        itineraryDetail1.setFoodAndHydrationNotes("Alimentación variada");
        itineraryDetail1.setGeneralRecommendations("Diviertanse");

        PackageItineraryDetail itineraryDetail2 = new PackageItineraryDetail();
        itineraryDetail2.setDuration("3 Días");
        itineraryDetail2.setLodgingType("Cabaña");
        itineraryDetail2.setTransferType("Terrestre");
        itineraryDetail2.setDailyActivitiesDescription("Actividades 2");
        itineraryDetail2.setFoodAndHydrationNotes("Notas 2");
        itineraryDetail2.setGeneralRecommendations("Recomendaciones 2");

        PackageImage image1 = new PackageImage();
        image1.setUrl("https://ruta/imagen_patagonia_principal.jpg");
        image1.setPrincipal(true);

        PackageImage image2 = new PackageImage();
        image2.setUrl("https://ruta/imagen_cordoba_principal.jpg");
        image2.setPrincipal(true);

        packageTravel1 = new PackageTravel();
        packageTravel1.setName("Aventura Patagonia");
        packageTravel1.setBasePrice(1500.00);
        packageTravel1.setDestination("Patagonia Argentina");
        packageTravel1.setShortDescription("Descripción corta aventura.");
        packageTravel1.setCategory(PackageCategory.GEOPAISAJES);
        packageTravel1.setItineraryDetail(itineraryDetail1);
        itineraryDetail1.setPackageTravel(packageTravel1);
        image1.setPackageTravel(packageTravel1);
        packageTravel1.setImages(List.of(image1));
        packageTravel1 = packageTravelRepository.save(packageTravel1);

        packageTravel2 = new PackageTravel();
        packageTravel2.setName("Relajación Cordobesa");
        packageTravel2.setBasePrice(800.50);
        packageTravel2.setDestination("Sierras de Córdoba");
        packageTravel2.setShortDescription("Descripción corta relajación.");
        packageTravel2.setCategory(PackageCategory.RELAJACION);
        packageTravel2.setItineraryDetail(itineraryDetail2);
        itineraryDetail2.setPackageTravel(packageTravel2);
        image2.setPackageTravel(packageTravel2);
        packageTravel2.setImages(List.of(image2));
        packageTravelRepository.save(packageTravel2);
    }

    @Test
    void findByCategory() {
        List<PackageTravel> foundPackages = packageTravelRepository.findByCategory(PackageCategory.GEOPAISAJES);
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