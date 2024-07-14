package com.github.kasiaOsowska.homeassistant.library.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.github.kasiaOsowska.homeassistant.library.model.Book;
import java.util.List;
import com.github.kasiaOsowska.homeassistant.library.model.StorageLocation;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByTitleContainingIgnoreCase(String title);
    List<Book> findByAuthorContainingIgnoreCase(String author);
    List<Book> findByStorageLocation(StorageLocation storageLocation);
    List<Book> findByGenreContainingIgnoreCase(String genre);
    @Query("SELECT b.title FROM Book b WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(b.author) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<String> findTitlesByTitleOrAuthorContainingIgnoreCase(String query);
}
