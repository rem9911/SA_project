package com.example.bookservice.controller;

import com.example.bookservice.kafka.BookProducer;
import com.example.bookservice.model.Book;
import com.example.bookservice.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Book Service", description = "Endpoints for managing books")
@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;
    private final BookProducer bookProducer;

    public BookController(BookService bookService, BookProducer bookProducer) {
        this.bookService = bookService;
        this.bookProducer = bookProducer;
    }

    @Operation(summary = "Retrieve all books", description = "Returns a list of all books")
    @GetMapping
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    @Operation(summary = "Retrieve a book by its ID", description = "Returns the details of a specific book")
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        return bookService.getBookById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create a new book", description = "Creates a new book and sets its availability to true")
    @PostMapping
    public Book createBook(@RequestBody Book book) {
        book.setAvailable(true); // By default, the book is available
        Book savedBook = bookService.saveBook(book);
        bookProducer.sendBookEvent("Book created: " + savedBook.getTitle());
        return savedBook;
    }

    @Operation(summary = "Delete a book", description = "Deletes the book identified by the provided ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        bookProducer.sendBookEvent("Book deleted with ID: " + id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Update book availability", description = "Updates the availability status of a book")
    @PutMapping("/{id}/availability")
    public ResponseEntity<Book> updateAvailability(@PathVariable Long id, @RequestParam boolean available) {
        return bookService.getBookById(id)
                .map(book -> {
                    book.setAvailable(available);
                    bookService.saveBook(book);
                    return ResponseEntity.ok(book);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Update a book", description = "Updates the details of an existing book")
    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody Book updatedBook) {
        return bookService.getBookById(id)
                .map(book -> {
                    book.setTitle(updatedBook.getTitle());
                    book.setAuthor(updatedBook.getAuthor());
                    book.setAvailable(updatedBook.isAvailable());
                    Book savedBook = bookService.saveBook(book);
                    bookProducer.sendBookEvent("Book updated: " + savedBook.getTitle());
                    return ResponseEntity.ok(savedBook);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
