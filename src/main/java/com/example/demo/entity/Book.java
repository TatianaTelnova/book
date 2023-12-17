package com.example.demo.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "book")
public class Book {
    @Id
    @Column(name = "id_book")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idBook;
    private String title;
    private String author;
    private Integer price;

    @Transient
    @OneToMany
    private List<Item> bookItems;

    public int getIdBook() {
        return idBook;
    }

    public void setIdBook(int idBook) {
        this.idBook = idBook;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;

        return this.idBook == book.getIdBook();
    }

    @Override
    public int hashCode() {
        return this.idBook;
    }

    public List<Item> getBookItems() {
        return bookItems;
    }

    public void setBookItems(List<Item> bookItems) {
        this.bookItems = bookItems;
    }
}
