package com.tierranativa.aplicacion.tierra.nativa.service.implement;

import com.tierranativa.aplicacion.tierra.nativa.dto.CategoryPackagesDTO;
import com.tierranativa.aplicacion.tierra.nativa.dto.ImageDTO;
import com.tierranativa.aplicacion.tierra.nativa.dto.ItineraryDetailDTO;
import com.tierranativa.aplicacion.tierra.nativa.dto.PackageTravelRequestDTO;
import com.tierranativa.aplicacion.tierra.nativa.entity.*;
import com.tierranativa.aplicacion.tierra.nativa.exception.ResourceAlreadyExistsException;
import com.tierranativa.aplicacion.tierra.nativa.exception.ResourceNotFoundException;
import com.tierranativa.aplicacion.tierra.nativa.repository.BookingRepository;
import com.tierranativa.aplicacion.tierra.nativa.repository.CategoryRepository;
import com.tierranativa.aplicacion.tierra.nativa.repository.PackageTravelRepository;
import com.tierranativa.aplicacion.tierra.nativa.repository.CharacteristicRepository;
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
    private final CharacteristicRepository characteristicRepository;
    private final BookingRepository bookingRepository;

    @Autowired
    public IPackageTravelService(PackageTravelRepository packageTravelRepository, CategoryRepository categoryRepository, CharacteristicRepository characteristicRepository, BookingRepository bookingRepository) {
        this.packageTravelRepository = packageTravelRepository;
        this.categoryRepository = categoryRepository;
        this.characteristicRepository = characteristicRepository;
        this.bookingRepository = bookingRepository;
    }

    @Override
    public Optional<PackageTravel> findById(Long id) {
        return packageTravelRepository.findById(id);
    }

    @Override
    @Transactional
    public PackageTravel registerNewPackage(PackageTravelRequestDTO requestDto, User admin) throws Exception {
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

        if (requestDto.getCharacteristicIds() != null && !requestDto.getCharacteristicIds().isEmpty()) {
            Set<Characteristics> characteristics = requestDto.getCharacteristicIds().stream()
                    .map(id -> characteristicRepository.findById(id)
                            .orElseThrow(() -> new ResourceNotFoundException("Característica con ID " + id + " no encontrada.")))
                    .collect(Collectors.toSet());
            newPackage.setCharacteristics(characteristics);
        }

        if (requestDto.getItineraryDetail() != null) {
            ItineraryDetailDTO detailDto = requestDto.getItineraryDetail();
            PackageItineraryDetail itineraryDetailEntity = new PackageItineraryDetail();
            itineraryDetailEntity.setDuration(detailDto.getDuration());
            itineraryDetailEntity.setLodgingType(detailDto.getLodgingType());
            itineraryDetailEntity.setTransferType(detailDto.getTransferType());
            itineraryDetailEntity.setDailyActivitiesDescription(detailDto.getDailyActivitiesDescription());
            itineraryDetailEntity.setFoodAndHydrationNotes(detailDto.getFoodAndHydrationNotes());
            itineraryDetailEntity.setGeneralRecommendations(detailDto.getGeneralRecommendations());
            itineraryDetailEntity.setPackageTravel(newPackage);
            newPackage.setItineraryDetail(itineraryDetailEntity);
        }

        if (requestDto.getImageDetails() != null && !requestDto.getImageDetails().isEmpty()) {
            for (ImageDTO imageDto : requestDto.getImageDetails()) {
                PackageImage imageEntity = PackageImage.builder()
                        .url(imageDto.getUrl())
                        .principal(imageDto.getPrincipal() != null ? imageDto.getPrincipal() : false)
                        .packageTravel(newPackage)
                        .build();
                newPackage.addImage(imageEntity);
            }
            String mainUrl = requestDto.getImageDetails().stream()
                    .filter(img -> img.getPrincipal() != null && img.getPrincipal())
                    .map(ImageDTO::getUrl)
                    .findFirst()
                    .orElse(requestDto.getImageDetails().get(0).getUrl());
            newPackage.setImageUrl(mainUrl);
        }

        PackageTravel savedPackage = packageTravelRepository.save(newPackage);

        if (requestDto.getAvailabilityBlocks() != null && !requestDto.getAvailabilityBlocks().isEmpty()) {
            List<Booking> blocks = requestDto.getAvailabilityBlocks().stream()
                    .map(blockDto -> Booking.builder()
                            .startDate(blockDto.getStartDate())
                            .endDate(blockDto.getEndDate())
                            .packageTravel(savedPackage)
                            .user(admin)
                            .status("CONFIRMED")
                            .build())
                    .collect(Collectors.toList());
            bookingRepository.saveAll(blocks);
        }

        return savedPackage;
    }

    @Override
    @Transactional
    public PackageTravel update(Long id, PackageTravelRequestDTO updateDto, User admin) throws Exception {
        PackageTravel existingPackage = packageTravelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paquete con Id: " + id + " no encontrado."));

        if (updateDto.getName() != null && packageTravelRepository.existsByNameAndIdNot(updateDto.getName(), id)) {
            throw new ResourceAlreadyExistsException("El nombre '" + updateDto.getName() + "' ya está en uso.");
        }

        if (updateDto.getName() != null) existingPackage.setName(updateDto.getName());
        if (updateDto.getBasePrice() != null) existingPackage.setBasePrice(updateDto.getBasePrice());
        if (updateDto.getShortDescription() != null) existingPackage.setShortDescription(updateDto.getShortDescription());
        if (updateDto.getDestination() != null) existingPackage.setDestination(updateDto.getDestination());

        if (updateDto.getCategoryId() != null) {
            Set<Category> categories = updateDto.getCategoryId().stream()
                    .map(catId -> categoryRepository.findById(catId)
                            .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada: " + catId)))
                    .collect(Collectors.toSet());
            existingPackage.setCategories(categories);
        }

        if (updateDto.getCharacteristicIds() != null) {
            Set<Characteristics> characteristics = updateDto.getCharacteristicIds().stream()
                    .map(charId -> characteristicRepository.findById(charId)
                            .orElseThrow(() -> new ResourceNotFoundException("Característica no encontrada: " + charId)))
                    .collect(Collectors.toSet());
            existingPackage.setCharacteristics(characteristics);
        }

        if (updateDto.getAvailabilityBlocks() != null) {
            List<Booking> currentBookings = bookingRepository.findByPackageTravelId(id);
            List<Booking> manualBlocks = currentBookings.stream()
                    .filter(b -> "CONFIRMED".equals(b.getStatus()))
                    .collect(Collectors.toList());
            bookingRepository.deleteAll(manualBlocks);

            List<Booking> newBlocks = updateDto.getAvailabilityBlocks().stream()
                    .map(blockDto -> Booking.builder()
                            .startDate(blockDto.getStartDate())
                            .endDate(blockDto.getEndDate())
                            .packageTravel(existingPackage)
                            .user(admin)
                            .status("CONFIRMED")
                            .build())
                    .collect(Collectors.toList());
            bookingRepository.saveAll(newBlocks);
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

    @Override
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
