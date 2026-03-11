package com.tierranativa.aplicacion.tierra.nativa.service.implement;

import com.tierranativa.aplicacion.tierra.nativa.dto.*;
import com.tierranativa.aplicacion.tierra.nativa.entity.*;
import com.tierranativa.aplicacion.tierra.nativa.exception.ResourceAlreadyExistsException;
import com.tierranativa.aplicacion.tierra.nativa.exception.ResourceNotFoundException;
import com.tierranativa.aplicacion.tierra.nativa.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IPackageTravelServiceTest {

    @Mock
    private PackageTravelRepository packageTravelRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CharacteristicRepository characteristicRepository;

    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private IPackageTravelService packageTravelService;

    @Captor
    private ArgumentCaptor<PackageTravel> packageTravelCaptor;

    private PackageTravel mockPackage;
    private PackageTravelRequestDTO validDto;
    private Category geoPaisajes;
    private Characteristics wifiEntity;
    private User adminUser;

    @BeforeEach
    void setUp() {
        geoPaisajes = Category.builder().id(1L).title("GEOPAISAJES").build();

        adminUser = User.builder()
                .id(1L)
                .email("admin@tierranativa.com")
                .role(RoleLogin.ADMIN)
                .build();

        mockPackage = new PackageTravel();
        mockPackage.setId(1L);
        mockPackage.setName("Paquete Test");
        mockPackage.setCategories(Set.of(geoPaisajes));

        wifiEntity = Characteristics.builder()
                .id(1L)
                .title("Wi-Fi")
                .icon("fa-wifi")
                .build();

        ItineraryDetailDTO detailDto = new ItineraryDetailDTO();
        detailDto.setDuration("5 Días");
        detailDto.setLodgingType("Hotel");
        detailDto.setTransferType("Vuelo");
        detailDto.setDailyActivitiesDescription("Act");
        detailDto.setFoodAndHydrationNotes("Comida");
        detailDto.setGeneralRecommendations("Tips");

        validDto = PackageTravelRequestDTO.builder()
                .name("Paquete Nuevo")
                .basePrice(1500.00)
                .shortDescription("Descripción obligatoria de más de diez caracteres.")
                .destination("Destino Test")
                .categoryId(Set.of(1L))
                .characteristicIds(Set.of(1L))
                .itineraryDetail(detailDto)
                .imageDetails(List.of(
                        ImageDTO.builder()
                                .url("http://img.url")
                                .principal(true)
                                .build()
                ))
                .build();
    }

    @Test
    void registerNewPackage_Success() throws Exception {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(geoPaisajes));
        when(characteristicRepository.findById(1L)).thenReturn(Optional.of(wifiEntity));
        when(packageTravelRepository.existsByName(anyString())).thenReturn(false);
        when(packageTravelRepository.save(any(PackageTravel.class))).thenReturn(mockPackage);

        packageTravelService.registerNewPackage(validDto, adminUser);

        verify(packageTravelRepository, times(1)).save(packageTravelCaptor.capture());
        PackageTravel savedPackage = packageTravelCaptor.getValue();

        assertThat(savedPackage.getName()).isEqualTo("Paquete Nuevo");
    }

    @Test
    void registerNewPackage_ThrowsException_WhenNameExists() {
        when(packageTravelRepository.existsByName(validDto.getName())).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class,
                () -> packageTravelService.registerNewPackage(validDto, adminUser));

        verify(packageTravelRepository, never()).save(any());
    }

    @Test
    void update_Success() throws Exception {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(geoPaisajes));
        when(characteristicRepository.findById(1L)).thenReturn(Optional.of(wifiEntity));
        when(packageTravelRepository.findById(1L)).thenReturn(Optional.of(mockPackage));
        when(packageTravelRepository.existsByNameAndIdNot(anyString(), anyLong())).thenReturn(false);
        when(packageTravelRepository.save(any(PackageTravel.class))).thenReturn(mockPackage);

        packageTravelService.update(1L, validDto, adminUser);

        verify(packageTravelRepository).save(any(PackageTravel.class));
    }

    @Test
    void delete_Success() {
        when(packageTravelRepository.findById(1L)).thenReturn(Optional.of(mockPackage));

        packageTravelService.delete(1L);

        verify(packageTravelRepository).deleteById(1L);
    }

    @Test
    void delete_ThrowsNotFound() {
        when(packageTravelRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> packageTravelService.delete(99L));
    }

    @Test
    void findByCategoryTitle_Success() {
        String title = "GEOPAISAJES";
        when(packageTravelRepository.findByCategories_Title(title)).thenReturn(List.of(mockPackage));

        List<PackageTravel> result = packageTravelService.findByCategoryTitle(title);

        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getName()).isEqualTo("Paquete Test");
    }

    @Test
    void findCategoryByTitle_Success() {
        String title = "GEOPAISAJES";
        when(categoryRepository.findByTitle(title)).thenReturn(Optional.of(geoPaisajes));

        Optional<Category> result = packageTravelService.findCategoryByTitle(title);

        assertThat(result).isPresent();
        assertThat(result.get().getTitle()).isEqualTo("GEOPAISAJES");
    }
}