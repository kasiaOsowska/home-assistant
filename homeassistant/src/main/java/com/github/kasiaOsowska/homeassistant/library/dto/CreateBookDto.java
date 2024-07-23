package com.github.kasiaOsowska.homeassistant.library.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateBookDto {
    private String title;
    private String author;
    private Long storageLocationId;
    private String genre;
    private Long userId;

    public CreateBookDto(String title, String author, Long storageLocation, String genre, Long userId) {
        this.title = title;
        this.author = author;
        this.storageLocationId = storageLocation;
        this.genre = genre;
        this.userId = userId;
    }
}