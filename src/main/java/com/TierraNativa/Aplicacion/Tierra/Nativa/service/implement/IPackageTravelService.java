package com.TierraNativa.Aplicacion.Tierra.Nativa.service.implement;

import com.TierraNativa.Aplicacion.Tierra.Nativa.entity.*;
import com.TierraNativa.Aplicacion.Tierra.Nativa.exception.ResourceAlreadyExistsException;
import com.TierraNativa.Aplicacion.Tierra.Nativa.exception.ResourceNotFoundException;
import com.TierraNativa.Aplicacion.Tierra.Nativa.repository.PackageTravelRepository;
import com.TierraNativa.Aplicacion.Tierra.Nativa.service.PackageTravelService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class IPackageTravelService implements PackageTravelService {

    private PackageTravelRepository packageTravelRepository;

    @Autowired
    public IPackageTravelService(PackageTravelRepository packageTravelRepository) {
        this.packageTravelRepository = packageTravelRepository;
    }

    @Override
    public Optional<PackageTravel> findById(Long id) {
        return packageTravelRepository.findById(id);
    }

    @Override
    public void update(PackageTravel packageTravel) {
        if (packageTravelRepository.existsByNameAndIdNot(packageTravel.getName(), packageTravel.getId())) {
            throw new ResourceAlreadyExistsException("El nombre del paquete '" + packageTravel.getName() + "' ya está en uso por otro producto.");
        }
        packageTravelRepository.save(packageTravel);
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
    public List<PackageTravel> findByCategory(String category) {
        return packageTravelRepository.findByCategory(category);
    }


    @Override
    public PackageTravel registerNewPackage(PackageTravelRequestDTO requestDto) throws Exception {

            if (packageTravelRepository.existsByName(requestDto.getName())) {
                throw new ResourceAlreadyExistsException("El nombre del paquete '" + requestDto.getName() + "' ya está en uso.");
            }

        PackageTravel newPackage = new PackageTravel();
        newPackage.setName(requestDto.getName());
        newPackage.setBasePrice(requestDto.getBasePrice());
        newPackage.setShortDescription(requestDto.getShortDescription());
        newPackage.setDestination(requestDto.getDestination());
        newPackage.setCategory(PackageCategory.valueOf(requestDto.getCategory()));
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
}