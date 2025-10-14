package dao;

import model.Book;
import java.sql.SQLException;
import java.util.List;

public interface IBookDAO {

    Book insert(Book book) throws SQLException;
    Book findByIsbn(String isbn) throws SQLException;
    List<Book> findAll() throws SQLException;
    boolean update(Book book) throws SQLException;

    // Filtering requirements
    List<Book> filterByCategory(String category) throws SQLException;
    List<Book> filterByAuthor(String author) throws SQLException;

    // Status management (Activation/Deactivation)
    boolean updateStatus(String isbn, boolean isActive) throws SQLException;

    // Stock management methods used during Loan/Return transactions
    boolean updateStock(String isbn, int change) throws SQLException;
}