package com.github.kasiaOsowska.homeassistant.library.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.github.kasiaOsowska.homeassistant.library.model.Book;
import com.github.kasiaOsowska.homeassistant.library.service.BookService;
import com.github.kasiaOsowska.homeassistant.library.dto.BookDto;
import com.github.kasiaOsowska.homeassistant.library.model.StorageLocation;
import com.github.kasiaOsowska.homeassistant.library.service.StorageLocationService;
import com.github.kasiaOsowska.homeassistant.library.service.OpenAIService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;
    private final StorageLocationService storageLocationService;
    private final OpenAIService openAIService;


    @Autowired
    public BookController(BookService bookService, StorageLocationService storageLocationService, OpenAIService openAIService) {
        this.bookService = bookService;
        this.storageLocationService = storageLocationService;
        this.openAIService = openAIService;
    }

    @PostMapping
    public ResponseEntity<Book> createBook(@RequestBody BookDto bookDto) {
        Optional<StorageLocation> storageLocation = storageLocationService.getStorageLocationById(bookDto.getStorageLocationId());
        if (!storageLocation.isPresent()) {
            return ResponseEntity.badRequest().build();
        }

        Book book = new Book();
        book.setTitle(bookDto.getTitle());
        book.setAuthor(bookDto.getAuthor());
        book.setStorageLocation(storageLocation.get());
        book.setGenre(bookDto.getGenre());

        return ResponseEntity.ok(bookService.saveBook(book));
    }

    @GetMapping
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        return bookService.getBookById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody BookDto bookDto) {
        Optional<StorageLocation> storageLocation = storageLocationService.getStorageLocationById(bookDto.getStorageLocationId());
        if (!storageLocation.isPresent()) {
            return ResponseEntity.badRequest().build();
        }

        Book bookDetails = new Book();
        bookDetails.setTitle(bookDto.getTitle());
        bookDetails.setAuthor(bookDto.getAuthor());
        bookDetails.setStorageLocation(storageLocation.get());
        bookDetails.setGenre(bookDto.getGenre());

        try {
            Book updatedBook = bookService.updateBook(id, bookDetails);
            return ResponseEntity.ok(updatedBook);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBookById(id);
        return ResponseEntity.noContent().build();
    }

    // Endpointy wyszukiwania
    @GetMapping("/search/storageLocation")
    public List<Book> getBooksByStorageLocation(@RequestParam Long storageLocationId) {
        Optional<StorageLocation> storageLocation = storageLocationService.getStorageLocationById(storageLocationId);
        return storageLocation.map(bookService::findByStorageLocation).orElseGet(List::of);
    }

    @GetMapping("/search/title")
    public List<Book> getBooksByTitle(@RequestParam String title) {
        return bookService.findBooksByTitle(title);
    }

    @GetMapping("/search/author")
    public List<Book> getBooksByAuthor(@RequestParam String author) {
        return bookService.findBooksByAuthor(author);
    }

    @GetMapping("/search/genre")
    public List<Book> getBooksByGenre(@RequestParam String genre) {
        return bookService.findBooksByGenre(genre);
    }

    @GetMapping("/autocomplete")
    public ResponseEntity<List<String>> autocompleteBooks(@RequestParam String query) {
        List<String> titles = bookService.autocompleteBookTitles(query);
        return ResponseEntity.ok(titles);
    }
    @PostMapping("/recommend")
    public ResponseEntity<String> recommendBook(@RequestBody String description) {
        List<Book> books = bookService.getAllBooks();
        try {
            String recommendation = openAIService.getBookRecommendation(description, books);
            return ResponseEntity.ok(recommendation);
        } catch (RuntimeException e) {
            return ResponseEntity.status(429).body(e.getMessage());
        }
    }
}
