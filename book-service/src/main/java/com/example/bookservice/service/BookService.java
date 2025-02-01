package com.example.bookservice.service;

import com.example.bookservice.kafka.BookProducer;
import com.example.bookservice.model.Book;
import com.example.bookservice.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final BookProducer bookProducer;

    public BookService(BookRepository bookRepository, BookProducer bookProducer) {
        this.bookRepository = bookRepository;
        this.bookProducer = bookProducer;
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    public Book createBook(Book book) {
        book.setAvailable(true); // By default, the book is available
        Book savedBook = bookRepository.save(book);
        bookProducer.sendBookEvent("Book created: " + savedBook.getTitle());
        return savedBook;
    }

    public boolean deleteBook(Long id) {
        if (bookRepository.existsById(id)) {
            bookRepository.deleteById(id);
            bookProducer.sendBookEvent("Book deleted with ID: " + id);
            return true;
        }
        return false;
    }

    public Optional<Book> updateAvailability(Long id, boolean available) {
        return bookRepository.findById(id).map(book -> {
            book.setAvailable(available);
            Book updatedBook = bookRepository.save(book);
            return updatedBook;
        });
    }

    public Optional<Book> updateBook(Long id, Book updatedBook) {
        return bookRepository.findById(id).map(existingBook -> {
            existingBook.setTitle(updatedBook.getTitle());
            existingBook.setAuthor(updatedBook.getAuthor());
            existingBook.setAvailable(updatedBook.isAvailable());
            Book savedBook = bookRepository.save(existingBook);
            bookProducer.sendBookEvent("Book updated: " + savedBook.getTitle());
            return savedBook;
        });
    }
}