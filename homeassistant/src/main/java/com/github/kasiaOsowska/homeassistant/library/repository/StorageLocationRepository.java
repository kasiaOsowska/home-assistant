package com.github.kasiaOsowska.homeassistant.library.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.github.kasiaOsowska.homeassistant.library.model.StorageLocation;

import java.util.Optional;

@Repository
public interface StorageLocationRepository extends JpaRepository<StorageLocation, Long> {
}