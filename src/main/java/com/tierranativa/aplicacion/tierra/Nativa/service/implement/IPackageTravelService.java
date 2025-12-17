package com.tierranativa.aplicacion.tierra.nativa.service.implement;

import com.tierranativa.aplicacion.tierra.nativa.dto.CategoryPackagesDTO;
import com.tierranativa.aplicacion.tierra.nativa.dto.ImageDTO;
import com.tierranativa.aplicacion.tierra.nativa.dto.ItineraryDetailDTO;
import com.tierranativa.aplicacion.tierra.nativa.dto.PackageTravelRequestDTO;
import com.tierranativa.aplicacion.tierra.nativa.entity.*;
import com.tierranativa.aplicacion.tierra.nativa.exception.ResourceAlreadyExistsException;
import com.tierranativa.aplicacion.tierra.nativa.exception.ResourceNotFoundException;
import com.tierranativa.aplicacion.tierra.nativa.repository.CategoryRepository;
import com.tierranativa.aplicacion.tierra.nativa.repository.PackageTravelRepository;
import com.tierranativa.aplicacion.tierra.nativa.service.PackageTravelService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class IPackageTravelService implements PackageTravelService {

    private final PackageTravelRepository packageTravelRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public IPackageTravelService(PackageTravelRepository packageTravelRepository, CategoryRepository categoryRepository) {
        this.packageTravelRepository = packageTravelRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Optional<PackageTravel> findById(Long id) {
        return packageTravelRepository.findById(id);
    }

    @Override
    public PackageTravel registerNewPackage(PackageTravelRequestDTO requestDto) {

        if (packageTravelRepository.existsByName(requestDto.getName())) {
            throw new ResourceAlreadyExistsException("El nombre del paquete '" + requestDto.getName() + "' ya está en uso.");
        }

        PackageTravel newPackage = new PackageTravel();
        newPackage.setName(requestDto.getName());
        newPackage.setBasePrice(requestDto.getBasePrice());
        newPackage.setShortDescription(requestDto.getShortDescription());
        newPackage.setDestination(requestDto.getDestination());

        Set<Category> categories = requestDto.getCategoryId().stream()
                .map(id -> categoryRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Categoría con ID " + id + " no encontrada.")))
                .collect(Collectors.toSet());
        newPackage.setCategories(categories);

        if (requestDto.getImageDetails() != null && !requestDto.getImageDetails().isEmpty()) {
            newPackage.setImageUrl(requestDto.getImageDetails().get(0).getUrl());
        }

        if (requestDto.getItineraryDetail() != null) {
            ItineraryDetailDTO detailDto = requestDto.getItineraryDetail();
            PackageItineraryDetail itineraryDetailEntity = PackageItineraryDetail.builder()
                    .duration(detailDto.getDuration())
                    .lodgingType(detailDto.getLodgingType())
                    .transferType(detailDto.getTransferType())
                    .dailyActivitiesDescription(detailDto.getDailyActivitiesDescription())
                    .foodAndHydrationNotes(detailDto.getFoodAndHydrationNotes())
                    .generalRecommendations(detailDto.getGeneralRecommendations())
                    .build();

            itineraryDetailEntity.setPackageTravel(newPackage);
            newPackage.setItineraryDetail(itineraryDetailEntity);
        }

        if (requestDto.getImageDetails() != null) {
            for (ImageDTO imageDto : requestDto.getImageDetails()) {
                PackageImage imageEntity = PackageImage.builder()
                        .url(imageDto.getUrl())
                        .principal(imageDto.getPrincipal() != null ? imageDto.getPrincipal() : false)
                        .build();
                newPackage.addImage(imageEntity);
            }
        }
        return packageTravelRepository.save(newPackage);
    }

    @Override
    public PackageTravel update(PackageTravel packageTravel) {
        if (packageTravelRepository.existsByNameAndIdNot(packageTravel.getName(), packageTravel.getId())) {
            throw new ResourceAlreadyExistsException("El nombre del paquete '" + packageTravel.getName() + "' ya está en uso por otro producto.");
        }
        PackageTravel existingPackage = packageTravelRepository.findById(packageTravel.getId())
                .orElseThrow(() -> new ResourceNotFoundException("No se pudo actualizar, el Paquete de Viaje con Id: " + packageTravel.getId() + " no fue encontrado."));

        existingPackage.setName(packageTravel.getName());
        existingPackage.setBasePrice(packageTravel.getBasePrice());
        existingPackage.setShortDescription(packageTravel.getShortDescription());
        existingPackage.setDestination(packageTravel.getDestination());
        existingPackage.setCategories(packageTravel.getCategories());

        List<PackageImage> updatedImages = packageTravel.getImages();
        if (updatedImages != null && !updatedImages.isEmpty()) {
            existingPackage.setImageUrl(updatedImages.get(0).getUrl());
        } else {
            existingPackage.setImageUrl(null);
        }
        existingPackage.syncImages(updatedImages);

        PackageItineraryDetail updatedDetail = packageTravel.getItineraryDetail();
        if (updatedDetail != null) {
            existingPackage.setItineraryDetail(updatedDetail);
        }
        return packageTravelRepository.save(existingPackage);
    }

    @Override
    public void delete(Long id) {
        Optional<PackageTravel> packageTravelToLookFor = findById(id);
        if (packageTravelToLookFor.isPresent()) {
            packageTravelRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException("No se pudo eliminar el Paquete de Viaje correspondiente al Id: " + id);
        }
    }

    @Override
    public List<PackageTravel> findAll() {
        return packageTravelRepository.findAll();
    }

    @Override
    public List<PackageTravel> findByCategoryTitle(String categoryTitle) {
        return packageTravelRepository.findByCategories_Title(categoryTitle);
    }

    public Optional<Category> findCategoryByTitle(String categoryTitle) {
        return categoryRepository.findByTitle(categoryTitle);
}

    public CategoryPackagesDTO getCategoryPackagesDTO(String categoryTitle) {
        Category category = categoryRepository.findByTitle(categoryTitle)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría con título '" + categoryTitle + "' no encontrada."));
        List<PackageTravel> packages = packageTravelRepository.findByCategories_Title(categoryTitle);

        return CategoryPackagesDTO.from(category, packages);
    }
}