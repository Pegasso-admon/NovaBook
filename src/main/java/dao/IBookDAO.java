package dao;

import model.Book;
import java.sql.Connection; // Importar Connection
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

    /**
     * Updates the available stock of a book. Used in Loan/Return transactions.
     * CRITICAL FIX: Accepts an existing Connection for transaction management.
     * @param isbn The ISBN of the book to update.
     * @param change The value to add (+1 for return, -1 for loan).
     * @param conn The JDBC connection controlled by the Service layer.
     * @return true if the stock update was successful.
     * @throws SQLException Database access error.
     */
    boolean updateStock(String isbn, int change, Connection conn) throws SQLException; // <-- CORREGIDO
}