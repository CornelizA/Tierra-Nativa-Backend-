package com.tierranativa.aplicacion.tierra.nativa.Service.implement;

import com.tierranativa.aplicacion.tierra.nativa.dto.ImageDTO;
import com.tierranativa.aplicacion.tierra.nativa.dto.ItineraryDetailDTO;
import com.tierranativa.aplicacion.tierra.nativa.dto.PackageTravelRequestDTO;
import com.tierranativa.aplicacion.tierra.nativa.entity.*;
import com.tierranativa.aplicacion.tierra.nativa.exception.ResourceAlreadyExistsException;
import com.tierranativa.aplicacion.tierra.nativa.exception.ResourceNotFoundException;
import com.tierranativa.aplicacion.tierra.nativa.repository.PackageTravelRepository;
import com.tierranativa.aplicacion.tierra.nativa.service.implement.IPackageTravelService;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IPackageTravelServiceTest {

    @Mock
    private PackageTravelRepository packageTravelRepository;

    @InjectMocks
    private IPackageTravelService packageTravelService;

    @Captor
    private ArgumentCaptor<PackageTravel> packageTravelCaptor;
    private PackageTravel mockPackage;
    private PackageTravelRequestDTO validDto;

    @BeforeEach
    void setUp() {

        mockPackage = new PackageTravel();
        mockPackage.setId(1L);
        mockPackage.setName("Paquete Test");
        mockPackage.setBasePrice(20000.00);
        mockPackage.setDestination("Corrientes");
        mockPackage.setShortDescription("Descripción corta relajación.");
        mockPackage.setCategory(Category.RELAJACION);

        ItineraryDetailDTO detailDto = new ItineraryDetailDTO();
        detailDto.setDuration("5 Días");
        detailDto.setLodgingType("Hotel");
        detailDto.setTransferType("Vuelos-Terrestres");
        detailDto.setDailyActivitiesDescription("Actividades diarias");
        detailDto.setFoodAndHydrationNotes("Alimentación variada");
        detailDto.setGeneralRecommendations("Diviertanse");

        ImageDTO imageDto = new ImageDTO();
        imageDto.setUrl("http://image.url/principal.jpg");
        imageDto.setPrincipal(true);

        validDto = new PackageTravelRequestDTO(
                "Paquete Nuevo",
                detailDto,
                1500.00,
                List.of(imageDto),
                "Desc breve",
                "Destino",
                "RELAJACION"
        );
    }

    @Test
    void registerNewPackage() throws Exception {
        when(packageTravelRepository.existsByName(anyString())).thenReturn(false);
        when(packageTravelRepository.save(any(PackageTravel.class))).thenReturn(mockPackage);
        packageTravelService.registerNewPackage(validDto);
        verify(packageTravelRepository, times(1)).save(packageTravelCaptor.capture());
        PackageTravel packageToSave = packageTravelCaptor.getValue();
        assertThat(packageToSave.getName()).isEqualTo(validDto.getName());
        assertThat(packageToSave.getItineraryDetail().getDuration()).isEqualTo("5 Días");
        assertThat(packageToSave.getImageUrl()).isEqualTo("http://image.url/principal.jpg");
    }

    @Test
    void registerNewPackage_ResourceAlreadyExistsException_NombreExiste() {
        when(packageTravelRepository.existsByName(anyString())).thenReturn(true);
        assertThrows(ResourceAlreadyExistsException.class,
                () -> packageTravelService.registerNewPackage(validDto));
        verify(packageTravelRepository, never()).save(any());
    }

    @Test
    void updatePackageTravel() {
        mockPackage.setName("Nombre Editado");
        when(packageTravelRepository.existsByNameAndIdNot(anyString(), anyLong())).thenReturn(false);
        packageTravelService.update(mockPackage);
        verify(packageTravelRepository, times(1)).save(mockPackage);
    }

    @Test
    void update_ResourceAlreadyExistsException_NombreExiste() {
        when(packageTravelRepository.existsByNameAndIdNot(anyString(), anyLong())).thenReturn(true);
        assertThrows(ResourceAlreadyExistsException.class,
                () -> packageTravelService.update(mockPackage));
        verify(packageTravelRepository, never()).save(any());
    }

    @Test
    void deletePackageTravel() {
        when(packageTravelRepository.findById(1L)).thenReturn(Optional.of(mockPackage));
        packageTravelService.delete(1L);
        verify(packageTravelRepository, times(1)).deleteById(1L);
    }

    @Test
    void delete_ResourceNotFoundException_NoExiste() {
        when(packageTravelRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,
                () -> packageTravelService.delete(99L));
        verify(packageTravelRepository, never()).deleteById(anyLong());
    }

    @Test
    void findByCategory() {
        when(packageTravelRepository.findByCategory(Category.GEOPAISAJES)).thenReturn(List.of(mockPackage));
        List<PackageTravel> result = packageTravelService.findByCategory(Category.GEOPAISAJES);
        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getId()).isEqualTo(1L);
        assertThat(result.get(0).getCategory()).isEqualTo(Category.RELAJACION);
    }
}