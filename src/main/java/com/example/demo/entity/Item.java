package com.example.demo.entity;

import javax.persistence.*;

@Entity
@Table(name = "book_items")
public class Item {
    @Id
    @Column(name = "id_book_items")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idBookItems;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_book")
    private Book book;
    private int count;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user_order")
    private Order bookItems;

    public Item() {
    }

    public Item(Book book, int count) {
        this.book = book;
        this.count = count;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Order getBookItems() {
        return bookItems;
    }

    public void setBookItems(Order bookItems) {
        this.bookItems = bookItems;
    }
}
