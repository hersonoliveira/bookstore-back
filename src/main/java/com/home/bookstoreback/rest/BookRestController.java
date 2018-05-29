package com.home.bookstoreback.rest;

import java.util.Collection;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.home.bookstoreback.infrastructure.BookNotFoundException;
import com.home.bookstoreback.model.Book;
import com.home.bookstoreback.model.BookRepository;

@RestController
@RequestMapping("/books")
public class BookRestController {

    private final BookRepository bookRepository;

    public BookRestController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @RequestMapping(method = RequestMethod.GET)
    Collection<Book> listBooks(){
        return bookRepository.findAll();
    }

    @RequestMapping(method = RequestMethod.POST)
    ResponseEntity<?> addBook(@RequestBody Book book){
        Book result = bookRepository.save(new Book(book.getTitle(), book.getDescription()));
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(result.getId())
                .toUri());
        return new ResponseEntity<String>(null, httpHeaders, HttpStatus.CREATED);
    }

    @GetMapping("/{bookId}")
    Book readBook(@PathVariable Long bookId) {
        if(bookRepository.findById(bookId).isPresent()){
            return bookRepository.findById(bookId).get();
        }
        throw new BookNotFoundException(bookId.toString());
    }

}
