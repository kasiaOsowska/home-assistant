package com.github.kasiaOsowska.homeassistant.library.repository;

import com.github.kasiaOsowska.homeassistant.library.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByUsername(String username);
    Optional<AppUser> findByGuid(String guid);
    List<AppUser> findAllByIdNot(Long id);

}
