package com.home.bookstoreback.infrastructure;

public class BookNotFoundException extends RuntimeException {

    public BookNotFoundException(String message) {
        super("Could not find book id: " + message);
    }
}
