package com.github.kasiaOsowska.homeassistant.library.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class SharedBook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private AppUser owner;

    @ManyToOne
    @JoinColumn(name = "borrower_id", nullable = false)
    private AppUser borrower;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;
}
