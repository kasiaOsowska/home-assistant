package com.github.kasiaOsowska.homeassistant.library.service;

import com.github.kasiaOsowska.homeassistant.library.repository.SharedBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.github.kasiaOsowska.homeassistant.library.dto.BookDto;
import com.github.kasiaOsowska.homeassistant.library.model.Book;
import com.github.kasiaOsowska.homeassistant.library.repository.BookRepository;
import com.github.kasiaOsowska.homeassistant.library.model.StorageLocation;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final SharedBookRepository sharedBookRepository;

    @Autowired
    public BookService(BookRepository bookRepository, SharedBookRepository sharedBookRepository) {
        this.bookRepository = bookRepository;
        this.sharedBookRepository = sharedBookRepository;
    }

    public List<BookDto> getAllBooks() {
        List<Book> books = bookRepository.findAll();
        return books.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<BookDto> getBooksByUserId(Long userId) {
        List<Book> books = bookRepository.findByUserId(userId);
        List<Book> borowedBooks = sharedBookRepository.findBorrowedBooksByUserId(userId);
        books.addAll(borowedBooks);
        return books.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Optional<BookDto> getBookById(Long id) {
        return bookRepository.findById(id)
                .map(this::convertToDto);
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

    public List<BookDto> findBooksByTitle(String title, Long userId) {
        List<Book> books = bookRepository.findByTitleAndUserId(title, userId);
        return books.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<BookDto> findBooksByAuthor(String author, Long userId) {
        List<Book> books = bookRepository.findByAuthorContainingIgnoreCaseAndUserId(author, userId);
        return books.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<BookDto> findByStorageLocation(StorageLocation storageLocation, Long userId) {
        List<Book> books = bookRepository.findByStorageLocationAndUserId(storageLocation, userId);
        return books.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<BookDto> findBooksByGenre(String genre, Long userId) {
        List<Book> books = bookRepository.findByGenreContainingIgnoreCaseAndUserId(genre, userId);
        return books.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<String> autocompleteBookTitles(String query, Long userId) {
        return bookRepository.findTitlesByTitleOrAuthorContainingIgnoreCaseAndUserId(query, userId);
    }

    private BookDto convertToDto(Book book) {
        return new BookDto(
                book.getTitle(),
                book.getAuthor(),
                book.getStorageLocation().getId(),
                book.getGenre()
        );
    }
}
