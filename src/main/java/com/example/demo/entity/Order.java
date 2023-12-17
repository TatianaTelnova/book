package com.example.demo.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "user_order")
public class Order {
    @Id
    @Column(name = "id_user_order")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idOrder;
    private int amount;
    private Integer idUser;
    private String phone;
    private String status;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "bookItems")
    private List<Item> bookItems;

    public Order(){
    }

    public Order(int idOrder, String status) {
        this.idOrder = idOrder;
        this.status = status;
    }
    public int getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(int idOrder) {
        this.idOrder = idOrder;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Integer getIdUser() {
        return idUser;
    }

    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }

    public List<Item> getBookItems() {
        return bookItems;
    }

    public void setBookItems(List<Item> bookItems) {
        this.bookItems = bookItems;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
