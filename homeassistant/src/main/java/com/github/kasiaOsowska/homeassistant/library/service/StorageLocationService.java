package com.github.kasiaOsowska.homeassistant.library.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.github.kasiaOsowska.homeassistant.library.model.StorageLocation;
import com.github.kasiaOsowska.homeassistant.library.repository.StorageLocationRepository;

import java.util.List;
import java.util.Optional;

@Service
public class StorageLocationService {

    private final StorageLocationRepository storageLocationRepository;

    @Autowired
    public StorageLocationService(StorageLocationRepository storageLocationRepository) {
        this.storageLocationRepository = storageLocationRepository;
    }

    public List<StorageLocation> getAllStorageLocations() {
        return storageLocationRepository.findAll();
    }

    public Optional<StorageLocation> getStorageLocationById(Long id) {
        return storageLocationRepository.findById(id);
    }

    public StorageLocation saveStorageLocation(StorageLocation storageLocation) {
        return storageLocationRepository.save(storageLocation);
    }

    public void deleteStorageLocationById(Long id) {
        storageLocationRepository.deleteById(id);
    }

    public StorageLocation updateStorageLocation(Long id, StorageLocation storageLocationDetails) {
        return storageLocationRepository.findById(id).map(storageLocation -> {
            storageLocation.setName(storageLocationDetails.getName());
            return storageLocationRepository.save(storageLocation);
        }).orElseThrow(() -> new RuntimeException("StorageLocation not found with id " + id));
    }
}
