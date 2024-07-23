package com.github.kasiaOsowska.homeassistant.library.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppUserDto {
    private Long id;
    private String username;
    public AppUserDto(Long id, String username) {
        this.id = id;
        this.username = username;
    }
}