package com.github.kasiaOsowska.homeassistant.library.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookDto {
    private String title;
    private String author;
    private Long storageLocationId;
    private String genre;
}
