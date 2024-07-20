package com.github.kasiaOsowska.homeassistant.library.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.github.kasiaOsowska.homeassistant.library.model.StorageLocation;
import com.github.kasiaOsowska.homeassistant.library.service.StorageLocationService;

import java.util.List;

@RestController
@RequestMapping("home-assistant/api/storage-locations")
public class StorageLocationController {

    private final StorageLocationService storageLocationService;

    @Autowired
    public StorageLocationController(StorageLocationService storageLocationService) {
        this.storageLocationService = storageLocationService;
    }

    @PostMapping
    public StorageLocation createStorageLocation(@RequestBody StorageLocation storageLocation) {
        return storageLocationService.saveStorageLocation(storageLocation);
    }

    @GetMapping
    public List<StorageLocation> getAllStorageLocations() {
        return storageLocationService.getAllStorageLocations();
    }

    @PutMapping("/{id}")
    public ResponseEntity<StorageLocation> updateStorageLocation(@PathVariable Long id, @RequestBody StorageLocation storageLocationDetails) {
        try {
            StorageLocation updatedStorageLocation = storageLocationService.updateStorageLocation(id, storageLocationDetails);
            return ResponseEntity.ok(updatedStorageLocation);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStorageLocation(@PathVariable Long id) {
        storageLocationService.deleteStorageLocationById(id);
        return ResponseEntity.noContent().build();
    }
}
