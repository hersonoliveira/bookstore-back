package com.home.bookstoreback.model;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long>{
    @Override
    Optional<Book> findById(Long aLong);
}
