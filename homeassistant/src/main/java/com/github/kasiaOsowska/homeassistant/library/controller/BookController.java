package com.github.kasiaOsowska.homeassistant.library.controller;

import com.github.kasiaOsowska.homeassistant.library.dto.AppUserDto;
import com.github.kasiaOsowska.homeassistant.library.dto.BookDto;
import com.github.kasiaOsowska.homeassistant.library.dto.CreateBookDto;
import com.github.kasiaOsowska.homeassistant.library.model.AppUser;
import com.github.kasiaOsowska.homeassistant.library.model.Book;
import com.github.kasiaOsowska.homeassistant.library.model.StorageLocation;
import com.github.kasiaOsowska.homeassistant.library.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("home-assistant/api/books")
public class BookController {

    private final BookService bookService;
    private final StorageLocationService storageLocationService;
    private final SessionService sessionService;
    private final OpenAIService openAIService;

    @Autowired
    public BookController(BookService bookService, StorageLocationService storageLocationService, SessionService sessionService, OpenAIService openAIService) {
        this.bookService = bookService;
        this.storageLocationService = storageLocationService;
        this.sessionService = sessionService;
        this.openAIService = openAIService;
    }

    @PostMapping
    public ResponseEntity<BookDto> createBook(@RequestBody CreateBookDto createBookDto, @RequestParam String sessionId) {
        try {
            AppUser user = sessionService.getUserBySessionId(sessionId);
            if (user == null) {
                return ResponseEntity.status(401).build();
            }

            Optional<StorageLocation> storageLocation = storageLocationService.getStorageLocationById(createBookDto.getStorageLocationId());
            if (!storageLocation.isPresent()) {
                return ResponseEntity.badRequest().build();
            }

            Book book = new Book();
            book.setTitle(createBookDto.getTitle());
            book.setAuthor(createBookDto.getAuthor());
            book.setStorageLocation(storageLocation.get());
            book.setGenre(createBookDto.getGenre());
            book.setUser(user);

            Book savedBook = bookService.saveBook(book);

            BookDto savedBookDto = new BookDto(
                    savedBook.getTitle(),
                    savedBook.getAuthor(),
                    savedBook.getStorageLocation().getId(),
                    savedBook.getGenre()
            );

            return ResponseEntity.ok(savedBookDto);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error: " + e.getMessage());
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/by-user")
    public ResponseEntity<List<BookDto>> getAllBooksByUserID(@RequestParam String sessionId) {
        AppUser user = sessionService.getUserBySessionId(sessionId);
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(bookService.getBooksByUserId(user.getId()));
    }

    @GetMapping("/all")
    public ResponseEntity<List<BookDto>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDto> getBookById(@PathVariable Long id, @RequestParam String sessionId) {
        AppUser user = sessionService.getUserBySessionId(sessionId);
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        return bookService.getBookById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookDto> updateBook(@PathVariable Long id, @RequestBody BookDto bookDto, @RequestParam String sessionId) {
        AppUser user = sessionService.getUserBySessionId(sessionId);
        if (user == null) {
            return ResponseEntity.status(401).build();
        }

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
            BookDto updatedBookDto = new BookDto(
                    updatedBook.getTitle(),
                    updatedBook.getAuthor(),
                    updatedBook.getStorageLocation().getId(),
                    updatedBook.getGenre()
            );
            return ResponseEntity.ok(updatedBookDto);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id, @RequestParam String sessionId) {
        AppUser user = sessionService.getUserBySessionId(sessionId);
        if (user == null) {
            return ResponseEntity.status(401).build();
        }

        bookService.deleteBookById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search/storageLocation")
    public ResponseEntity<List<BookDto>> getBooksByStorageLocation(@RequestParam Long storageLocationId, @RequestParam String sessionId) {
        AppUser user = sessionService.getUserBySessionId(sessionId);
        if (user == null) {
            return ResponseEntity.status(401).build();
        }

        Optional<StorageLocation> storageLocation = storageLocationService.getStorageLocationById(storageLocationId);
        return storageLocation.map(location -> bookService.findByStorageLocation(location, user.getId()))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @GetMapping("/search/title")
    public ResponseEntity<List<BookDto>> getBooksByTitle(@RequestParam String title, @RequestParam String sessionId) {
        AppUser user = sessionService.getUserBySessionId(sessionId);
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(bookService.findBooksByTitle(title, user.getId()));
    }

    @GetMapping("/search/author")
    public ResponseEntity<List<BookDto>> getBooksByAuthor(@RequestParam String author, @RequestParam String sessionId) {
        AppUser user = sessionService.getUserBySessionId(sessionId);
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(bookService.findBooksByAuthor(author, user.getId()));
    }

    @GetMapping("/search/genre")
    public ResponseEntity<List<BookDto>> getBooksByGenre(@RequestParam String genre, @RequestParam String sessionId) {
        AppUser user = sessionService.getUserBySessionId(sessionId);
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(bookService.findBooksByGenre(genre, user.getId()));
    }

    @GetMapping("/autocomplete")
    public ResponseEntity<List<String>> autocompleteBooks(@RequestParam String query, @RequestParam String sessionId) {
        AppUser user = sessionService.getUserBySessionId(sessionId);
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(bookService.autocompleteBookTitles(query, user.getId()));
    }
    @PostMapping("/recommend")
    public ResponseEntity<String> recommendBook(@RequestBody String description, @RequestParam String sessionId) {
        AppUser user = sessionService.getUserBySessionId(sessionId);
        List<BookDto> books = bookService.getBooksByUserId(user.getId());
        try {
            String recommendation = openAIService.getBookRecommendation(description, books);
            return ResponseEntity.ok(recommendation);
        } catch (RuntimeException e) {
            return ResponseEntity.status(429).body(e.getMessage());
        }
    }

}
