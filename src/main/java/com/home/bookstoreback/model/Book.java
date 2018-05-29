package com.home.bookstoreback.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Book {

    @Id
    @GeneratedValue
    private Long id;

    private String title;

    private String description;

    private Float unitCost;

    private String isbn;

    private Date publicationDate;

    private Integer nbOfPages;

    private String imageUrl;

    private Language language;

    public Book() { }

    public Book(String title, String description, Float unitCost, String isbn, Date publicationDate, Integer nbOfPages, String imageUrl, Language language) {
        this.title = title;
        this.description = description;
        this.unitCost = unitCost;
        this.isbn = isbn;
        this.publicationDate = publicationDate;
        this.nbOfPages = nbOfPages;
        this.imageUrl = imageUrl;
        this.language = language;
    }

    public Book(String title, String description){
        this.title = title;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Float getUnitCost() {
        return unitCost;
    }

    public String getIsbn() {
        return isbn;
    }

    public Date getPublicationDate() {
        return publicationDate;
    }

    public Integer getNbOfPages() {
        return nbOfPages;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Language getLanguage() {
        return language;
    }
}
