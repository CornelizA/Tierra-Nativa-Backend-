
package com.tierranativa.aplicacion.tierra.nativa.repository;

import com.tierranativa.aplicacion.tierra.nativa.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class PackageTravelRepositoryTest {

    @Autowired
    private PackageTravelRepository packageTravelRepository;

    @Autowired
    private TestEntityManager entityManager;

    private PackageTravel packageTravel1;
    private PackageTravel packageTravel2;
    private Category geoPaisajes;
    private Category relajacion;
    private Characteristics characteristics;

    @BeforeEach
    void setUp() {
        geoPaisajes = Category.builder()
                .title("GEOPAISAJES")
                .description("Descripción para la categoría de geopaisajes")
                .imageUrl("https://images.pexels.com/photos/13659148/pexels-photo-13659148.jpeg")
                .build();

        relajacion = Category.builder()
                .title("RELAJACION")
                .description("Descripción para la categoría de relajación")
                .imageUrl("https://images.pexels.com/photos/13659148/pexels-photo-13659148.jpeg")
                .build();

        Characteristics wifi = Characteristics.builder()
                .title("Wi-Fi")
                .icon("fa-wifi")
                .build();

        entityManager.persist(geoPaisajes);
        entityManager.persist(relajacion);
        entityManager.persist(wifi);

        PackageItineraryDetail detail1 = PackageItineraryDetail.builder()
                .duration("7 Días")
                .lodgingType("Hotel")
                .transferType("Vuelo")
                .dailyActivitiesDescription("Actividades varias")
                .foodAndHydrationNotes("Notas de comida")
                .build();

        packageTravel1 = new PackageTravel();
        packageTravel1.setName("Aventura Patagonia");
        packageTravel1.setBasePrice(1500.00);
        packageTravel1.setDestination("Patagonia Argentina");
        packageTravel1.setShortDescription("Una breve descripción de la aventura.");
        packageTravel1.setCategories(Set.of(geoPaisajes));
        packageTravel1.setCharacteristics(Set.of(wifi));
        packageTravel1.setItineraryDetail(detail1);
        packageTravel1.addImage(PackageImage.builder()
                .url("https://ruta/imagen_patagonia_principal.jpg")
                .principal(true)
                .build());

        PackageItineraryDetail detail2 = PackageItineraryDetail.builder()
                .duration("3 Días")
                .lodgingType("Cabaña")
                .transferType("Bus")
                .dailyActivitiesDescription("Actividades relax")
                .foodAndHydrationNotes("Notas de dieta")
                .build();

        packageTravel2 = new PackageTravel();
        packageTravel2.setName("Relajación Cordobesa");
        packageTravel2.setBasePrice(800.50);
        packageTravel2.setDestination("Sierras de Córdoba");
        packageTravel2.setShortDescription("Una breve descripción de la relajación.");
        packageTravel2.setCategories(Set.of(relajacion));
        packageTravel2.setCharacteristics(Set.of(wifi));
        packageTravel2.setItineraryDetail(detail2);
        packageTravel2.addImage(PackageImage.builder()
                .url("https://ruta/imagen_cordoba_principal.jpg")
                .principal(true)
                .build());

        packageTravel1 = packageTravelRepository.save(packageTravel1);
        packageTravel2 = packageTravelRepository.save(packageTravel2);
        entityManager.flush();
    }

    @Test
    void testFindByCategories_Title() {
        List<PackageTravel> found = packageTravelRepository.findByCategories_Title("GEOPAISAJES");

        assertThat(found).hasSize(1);
        assertThat(found.get(0).getName()).isEqualTo("Aventura Patagonia");
    }

    @Test
    void testFindByCategoriesContaining() {
        List<PackageTravel> found = packageTravelRepository.findByCategoriesContaining(geoPaisajes);

        assertThat(found).hasSize(1);
        assertThat(found.get(0).getName()).isEqualTo("Aventura Patagonia");
        assertThat(found.get(0).getImages()).isNotEmpty();
    }

    @Test
    void testExistsByName() {
        assertThat(packageTravelRepository.existsByName("Aventura Patagonia")).isTrue();
        assertThat(packageTravelRepository.existsByName("Inexistente")).isFalse();
    }

    @Test
    void testExistsByNameAndIdNot() {
        boolean exists = packageTravelRepository.existsByNameAndIdNot("Aventura Patagonia", packageTravel2.getId());
        assertThat(exists).isTrue();

        boolean existsSelf = packageTravelRepository.existsByNameAndIdNot("Aventura Patagonia", packageTravel1.getId());
        assertThat(existsSelf).isFalse();
    }
}