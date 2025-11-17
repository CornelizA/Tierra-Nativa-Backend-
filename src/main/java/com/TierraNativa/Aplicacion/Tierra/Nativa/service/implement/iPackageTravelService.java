package com.tierranativa.aplicacion.tierra.nativa.service.implement;

import com.tierranativa.aplicacion.tierra.nativa.entity.*;
import com.tierranativa.aplicacion.tierra.nativa.exception.resourceAlreadyExistsException;
import com.tierranativa.aplicacion.tierra.nativa.exception.resourceNotFoundException;
import com.tierranativa.aplicacion.tierra.nativa.repository.packageTravelRepository;
import com.tierranativa.aplicacion.tierra.nativa.service.packageTravelService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class iPackageTravelService implements packageTravelService {

    private final packageTravelRepository packageTravelRepository;

    @Autowired
    public iPackageTravelService(packageTravelRepository packageTravelRepository) {
        this.packageTravelRepository = packageTravelRepository;
    }

    @Override
    public Optional<packageTravel> findById(Long id) {
        return packageTravelRepository.findById(id);
    }

    @Override
    public packageTravel update(packageTravel packageTravel) {
        if (packageTravelRepository.existsByNameAndIdNot(packageTravel.getName(), packageTravel.getId())) {
            throw new resourceAlreadyExistsException("El nombre del paquete '" + packageTravel.getName() + "' ya está en uso por otro producto.");
        }
        return packageTravelRepository.save(packageTravel);
    }

    @Override
    public void delete(Long id) {
        Optional<packageTravel> packageTravelToLookFor = findById(id);
        if (packageTravelToLookFor.isPresent()) {
            packageTravelRepository.deleteById(id);
        } else {
            throw new resourceNotFoundException("No se pudo eliminar el Paquete de Viaje correspondiente al Id: " + id);
        }
    }

    @Override
    public List<packageTravel> findAll() {
        return packageTravelRepository.findAll();
    }

    @Override
    public List<packageTravel> findByCategory(packageCategory category) {
        return packageTravelRepository.findByCategory(category);
    }


    @Override
    public packageTravel registerNewPackage(packageTravelRequestDTO requestDto) {

            if (packageTravelRepository.existsByName(requestDto.getName())) {
                throw new resourceAlreadyExistsException("El nombre del paquete '" + requestDto.getName() + "' ya está en uso.");
            }

        packageTravel newPackage = new packageTravel();
        newPackage.setName(requestDto.getName());
        newPackage.setBasePrice(requestDto.getBasePrice());
        newPackage.setShortDescription(requestDto.getShortDescription());
        newPackage.setDestination(requestDto.getDestination());
        newPackage.setCategory(packageCategory.valueOf(requestDto.getCategory()));
        if (requestDto.getImageDetails() != null && !requestDto.getImageDetails().isEmpty()) {
            newPackage.setImageUrl(requestDto.getImageDetails().get(0).getUrl());
        }

        if (requestDto.getItineraryDetail() != null) {
            itineraryDetailDTO detailDto = requestDto.getItineraryDetail();
            packageItineraryDetail itineraryDetailEntity = packageItineraryDetail.builder()
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
            for (imageDTO imageDto : requestDto.getImageDetails()) {
                packageImage imageEntity = packageImage.builder()
                        .url(imageDto.getUrl())
                        .principal(imageDto.getPrincipal() != null ? imageDto.getPrincipal() : false)
                        .build();
                newPackage.addImage(imageEntity);
            }
        }

        return packageTravelRepository.save(newPackage);
    }
}