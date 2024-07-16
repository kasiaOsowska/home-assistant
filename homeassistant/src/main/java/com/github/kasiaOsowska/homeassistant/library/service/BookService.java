package com.github.kasiaOsowska.homeassistant.library.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.github.kasiaOsowska.homeassistant.library.model.Book;
import com.github.kasiaOsowska.homeassistant.library.repository.BookRepository;
import com.github.kasiaOsowska.homeassistant.library.model.StorageLocation;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }

    public void deleteBookById(Long id) {
        bookRepository.deleteById(id);
    }

    public Book updateBook(Long id, Book bookDetails) {
        return bookRepository.findById(id).map(book -> {
            book.setTitle(bookDetails.getTitle());
            book.setAuthor(bookDetails.getAuthor());
            book.setStorageLocation(bookDetails.getStorageLocation());
            book.setGenre(bookDetails.getGenre());
            return bookRepository.save(book);
        }).orElseThrow(() -> new RuntimeException("Book not found with id " + id));
    }

    // Metody wyszukiwania
    public List<Book> findBooksByTitle(String title) {
        return bookRepository.findByTitle(title);
    }

    public List<Book> findBooksByAuthor(String author) {
        return bookRepository.findByAuthorContainingIgnoreCase(author);
    }

    public List<Book> findByStorageLocation(StorageLocation storageLocation) {
        return bookRepository.findByStorageLocation(storageLocation);
    }

    public List<Book> findBooksByGenre(String genre) {
        return bookRepository.findByGenreContainingIgnoreCase(genre);
    }
    public List<String> autocompleteBookTitles(String query) {
        return bookRepository.findTitlesByTitleOrAuthorContainingIgnoreCase(query);
    }

}
