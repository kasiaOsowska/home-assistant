package com.github.kasiaOsowska.homeassistant.library.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.github.kasiaOsowska.homeassistant.library.model.Book;
import java.util.List;
import com.github.kasiaOsowska.homeassistant.library.model.StorageLocation;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByTitleAndUserId(String title, Long userId);
    List<Book> findByAuthorContainingIgnoreCaseAndUserId(String author, Long userId);
    List<Book> findByStorageLocationAndUserId(StorageLocation storageLocation, Long userId);
    List<Book> findByGenreContainingIgnoreCaseAndUserId(String genre, Long userId);

    @Query("SELECT b.title FROM Book b WHERE (LOWER(b.title) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(b.author) LIKE LOWER(CONCAT('%', :query, '%'))) AND b.user.id = :userId")
    List<String> findTitlesByTitleOrAuthorContainingIgnoreCaseAndUserId(String query, Long userId);

    List<Book>findByUserId(Long userId);

}