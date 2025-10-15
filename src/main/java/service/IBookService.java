package service;

import exception.BusinessException;
import model.Book;
import java.sql.SQLException;
import java.util.List;

public interface IBookService {
    Book register(Book book) throws BusinessException, SQLException;
    boolean update(Book book) throws BusinessException, SQLException;
    Book findByIsbn(String isbn) throws SQLException;
    List<Book> findAll() throws SQLException;

    // Filtering
    List<Book> filterByCategory(String category) throws SQLException;
    List<Book> filterByAuthor(String author) throws SQLException;

    // Status
    boolean updateStatus(String isbn, boolean isActive) throws SQLException;
}