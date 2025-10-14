package model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Book {
    private String isbn;
    private String title;
    private String author;
    private String category;
    private BigDecimal referencePrice;
    private int totalCopies;
    private int availableCopies;
    private boolean isActive;
    private Timestamp createdAt;

    public Book() {
    }

    public Book(String isbn, String title, String author, String category, BigDecimal referencePrice, int totalCopies, int availableCopies, boolean isActive, Timestamp createdAt) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.category = category;
        this.referencePrice = referencePrice;
        this.totalCopies = totalCopies;
        this.availableCopies = availableCopies;
        this.isActive = isActive;
        this.createdAt = createdAt;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BigDecimal getReferencePrice() {
        return referencePrice;
    }

    public void setReferencePrice(BigDecimal referencePrice) {
        this.referencePrice = referencePrice;
    }

    public int getTotalCopies() {
        return totalCopies;
    }

    public void setTotalCopies(int totalCopies) {
        this.totalCopies = totalCopies;
    }

    public int getAvailableCopies() {
        return availableCopies;
    }

    public void setAvailableCopies(int availableCopies) {
        this.availableCopies = availableCopies;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

//    public void setCreatedAt(Timestamp createdAt) {
//        this.createdAt = createdAt;
//    } //No creo que se use xd
}