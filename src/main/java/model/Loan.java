package model;

import java.math.BigDecimal;
import java.sql.Date; // For due_date and return_date (no time)
import java.sql.Timestamp; // For loan_date (with time)

public class Loan {
    private int id;
    private String bookIsbn;
    private int partnerId;
    private Timestamp loanDate;
    private Date dueDate;
    private Date returnDate;
    private BigDecimal fine;
    private boolean isReturned;

    public Loan() {
    }

    public Loan(int id, String bookIsbn, int partnerId, Timestamp loanDate, Date dueDate, Date returnDate, BigDecimal fine, boolean isReturned) {
        this.id = id;
        this.bookIsbn = bookIsbn;
        this.partnerId = partnerId;
        this.loanDate = loanDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.fine = fine;
        this.isReturned = isReturned;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBookIsbn() {
        return bookIsbn;
    }

    public void setBookIsbn(String bookIsbn) {
        this.bookIsbn = bookIsbn;
    }

    public int getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(int partnerId) {
        this.partnerId = partnerId;
    }

    public Timestamp getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(Timestamp loanDate) {
        this.loanDate = loanDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public boolean isReturned() {
        return isReturned;
    }

    public void setReturned(boolean returned) {
        isReturned = returned;
    }

    public BigDecimal getFine() {
        return fine;
    }

    public void setFine(BigDecimal fine) {
        this.fine = fine;
    }
}