package com.example.bookservice.service;

import com.example.bookservice.adapter.MessagePublisher;
import com.example.bookservice.exceptions.BookNotFoundException;
import com.example.bookservice.kafka.BookProducer;
import com.example.bookservice.model.Book;
import com.example.bookservice.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final MessagePublisher messagePublisher;

    public BookService(BookRepository bookRepository, MessagePublisher messagePublisher) {
        this.bookRepository = bookRepository;
        this.messagePublisher = messagePublisher;
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book getBookById(Long id) throws BookNotFoundException {
        return bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException("Book not found with ID: " + id));
    }

    public Book createBook(Book book) {
        book.setAvailable(true); // By default, the book is available
        Book savedBook = bookRepository.save(book);
        messagePublisher.sendMessage("Book created: " + savedBook.getTitle());
        return savedBook;
    }

    public Book updateBook(Long id, Book updatedBook) throws BookNotFoundException {
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book not found with ID: " + id));

        existingBook.setTitle(updatedBook.getTitle());
        existingBook.setAuthor(updatedBook.getAuthor());
        existingBook.setAvailable(updatedBook.isAvailable());

        Book savedBook = bookRepository.save(existingBook);
        messagePublisher.sendMessage("Book updated: " + savedBook.getTitle());
        return savedBook;
    }

    public boolean deleteBook(Long id) {
        if (bookRepository.existsById(id)) {
            bookRepository.deleteById(id);
            messagePublisher.sendMessage("Book deleted with ID: " + id);
            return true;
        }
        return false;
    }

    public Book updateAvailability(Long id, boolean available) throws BookNotFoundException {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book not found with ID: " + id));

        book.setAvailable(available);
        return bookRepository.save(book);
    }
}