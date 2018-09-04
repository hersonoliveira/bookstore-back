package com.home.bookstoreback.rest;

import com.home.bookstoreback.infrastructure.BookNotFoundException;
import com.home.bookstoreback.model.Book;
import com.home.bookstoreback.model.BookRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BookRestController {

    private final BookRepository bookRepository;

    public BookRestController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    // Root

    @GetMapping("/books")
    List<Book> listBooks() {
        return bookRepository.findAll();
    }

    @PostMapping("/books")
    Book addBook(@RequestBody Book book) {
        return bookRepository.save(book);
    }

    // Single item

    @GetMapping("books/{id}")
    Book readBook(@PathVariable Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
    }

    @PutMapping("/books/{id}")
    Book replaceBook(@RequestBody Book newBook, @PathVariable Long id) {
        return bookRepository.findById(id)
                .map(book -> {
                    book.setTitle(newBook.getTitle());
                    book.setDescription(newBook.getDescription());
                    return bookRepository.save(book);
                }).orElseGet(() -> {
                    newBook.setId(id);
                    return bookRepository.save(newBook);
                });
    }

    @DeleteMapping("/books/{id}")
    void deleteBook(@PathVariable Long id) {
        bookRepository.deleteById(id);
    }
}
