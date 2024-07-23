package com.github.kasiaOsowska.homeassistant.library.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String author;

    @ManyToOne
    private StorageLocation storageLocation;

    private String genre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private AppUser user;


    public Book(String title, String author, StorageLocation storageLocation, String genre) {
        this.title = title;
        this.author = author;
        this.storageLocation = storageLocation;
        this.genre = genre;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, author, storageLocation, genre);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(id, book.id) &&
                Objects.equals(title, book.title) &&
                Objects.equals(author, book.author) &&
                Objects.equals(storageLocation, book.storageLocation) &&
                Objects.equals(genre, book.genre);
    }
}
