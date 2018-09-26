package com.home.bookstoreback.rest;

import com.home.bookstoreback.infrastructure.BookNotFoundException;
import com.home.bookstoreback.model.Book;
import com.home.bookstoreback.model.BookRepository;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
public class BookRestController {

    private final BookRepository bookRepository;

    public BookRestController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    // Aggregate Root

    @GetMapping(value = "/books", produces = {MediaType.APPLICATION_JSON_VALUE, "application/hal+json"})
    Resources<Resource<Book>> listBooks() {
        List<Resource<Book>> books = bookRepository.findAll().stream()
                .map(book -> new Resource<>(book,
                        linkTo(methodOn(BookRestController.class).readBook(book.getId())).withSelfRel(),
                        linkTo(methodOn(BookRestController.class).listBooks()).withRel("books")))
                .collect(Collectors.toList());

        return new Resources<>(books,
                linkTo(methodOn(BookRestController.class).listBooks()).withSelfRel());
    }

    @PostMapping("/books")
    Book addBook(@RequestBody Book book) {
        return bookRepository.save(book);
    }

    // Single item

    @GetMapping(value = "books/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, "application/hal+json"})
    Resource<Book> readBook(@PathVariable Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));

        return new Resource<>(book,
                linkTo(methodOn(BookRestController.class).readBook(id)).withSelfRel(),
                linkTo(methodOn(BookRestController.class).listBooks()).withRel("books"));
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
        //TODO Refactor so instantiating a book obj is not needed
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
        bookRepository.delete(book);
    }
}
