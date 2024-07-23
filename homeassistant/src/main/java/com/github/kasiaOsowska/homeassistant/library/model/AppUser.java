package com.github.kasiaOsowska.homeassistant.library.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String guid;

    @OneToMany(mappedBy = "user")
    private Set<Book> books;

    public AppUser() {
        this.guid = UUID.randomUUID().toString();
    }

    @OneToMany(mappedBy = "owner")
    private Set<SharedBook> ownedSharedBooks;

    @OneToMany(mappedBy = "borrower")
    private Set<SharedBook> borrowedSharedBooks;
}
