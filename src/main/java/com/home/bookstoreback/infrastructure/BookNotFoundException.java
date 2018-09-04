package com.home.bookstoreback.infrastructure;

public class BookNotFoundException extends RuntimeException {

    public BookNotFoundException(Long id) {
        super("Could not find book id: " + id);
    }
}
