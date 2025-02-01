package com.example.bookservice.controller;

import com.example.bookservice.kafka.BookProducer;
import com.example.bookservice.model.Book;
import com.example.bookservice.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;
    private final BookProducer bookProducer;

    public BookController(BookService bookService, BookProducer bookProducer) {
        this.bookService = bookService;
        this.bookProducer = bookProducer;
    }

    @GetMapping
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        return bookService.getBookById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Book createBook(@RequestBody Book book) {
        book.setAvailable(true); // Par défaut, le livre est disponible
        Book savedBook = bookService.saveBook(book);
        bookProducer.sendBookEvent("Book created: " + savedBook.getTitle());
        return savedBook;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        bookProducer.sendBookEvent("Book deleted with ID: " + id);
        return ResponseEntity.noContent().build();
    }
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
    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody Book updatedBook) {
        return bookService.getBookById(id)
                .map(book -> {
                    book.setTitle(updatedBook.getTitle());
                    book.setAuthor(updatedBook.getAuthor());
                    book.setAvailable(updatedBook.isAvailable());
                    Book savedBook = bookService.saveBook(book);
                    bookProducer.sendBookEvent("Book updated: " + savedBook.getTitle());
                    return ResponseEntity.ok(book);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }




}
