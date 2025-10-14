package dao;

import model.Book;
import util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDAOImpl implements IBookDAO {

    // SQL Statements
    private static final String INSERT_SQL = "INSERT INTO books (isbn, title, author, category, reference_price, total_copies, available_copies, is_active) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String FIND_BY_ISBN_SQL = "SELECT * FROM books WHERE isbn = ?";
    private static final String FIND_ALL_SQL = "SELECT * FROM books";
    private static final String UPDATE_SQL = "UPDATE books SET title = ?, author = ?, category = ?, reference_price = ?, total_copies = ?, available_copies = ?, is_active = ? WHERE isbn = ?";
    private static final String UPDATE_STATUS_SQL = "UPDATE books SET is_active = ? WHERE isbn = ?";
    private static final String UPDATE_STOCK_SQL = "UPDATE books SET available_copies = available_copies + ? WHERE isbn = ?";
    private static final String FIND_BY_CATEGORY_SQL = "SELECT * FROM books WHERE category = ? AND is_active = TRUE";
    private static final String FIND_BY_AUTHOR_SQL = "SELECT * FROM books WHERE author = ? AND is_active = TRUE";

    // Utility method to map ResultSet to a Book object
    private Book mapResultSetToBook(ResultSet rs) throws SQLException {
        Book book = new Book();
        book.setIsbn(rs.getString("isbn"));
        book.setTitle(rs.getString("title"));
        book.setAuthor(rs.getString("author"));
        book.setCategory(rs.getString("category"));
        book.setReferencePrice(rs.getBigDecimal("reference_price"));
        book.setTotalCopies(rs.getInt("total_copies"));
        book.setAvailableCopies(rs.getInt("available_copies"));
        book.setActive(rs.getBoolean("is_active"));
        return book;
    }

    @Override
    public Book insert(Book book) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_SQL)) {
            // Set all book attributes in the prepared statement
            ps.setString(1, book.getIsbn());
            ps.setString(2, book.getTitle());
            ps.setString(3, book.getAuthor());
            ps.setString(4, book.getCategory());
            ps.setBigDecimal(5, book.getReferencePrice());
            ps.setInt(6, book.getTotalCopies());
            ps.setInt(7, book.getAvailableCopies());
            ps.setBoolean(8, book.isActive());

            ps.executeUpdate();
            return book;
        }
    }

    @Override
    public Book findByIsbn(String isbn) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(FIND_BY_ISBN_SQL)) {
            ps.setString(1, isbn);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToBook(rs);
                }
                return null; // Book not found
            }
        }
    }

    @Override
    public List<Book> findAll() throws SQLException {
        List<Book> books = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(FIND_ALL_SQL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                books.add(mapResultSetToBook(rs));
            }
        }
        return books;
    }

    @Override
    public boolean update(Book book) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_SQL)) {

            ps.setString(1, book.getTitle());
            ps.setString(2, book.getAuthor());
            ps.setString(3, book.getCategory());
            ps.setBigDecimal(4, book.getReferencePrice());
            ps.setInt(5, book.getTotalCopies());
            ps.setInt(6, book.getAvailableCopies());
            ps.setBoolean(7, book.isActive());
            ps.setString(8, book.getIsbn()); // WHERE clause

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public List<Book> filterByCategory(String category) throws SQLException {
        List<Book> books = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(FIND_BY_CATEGORY_SQL)) {
            ps.setString(1, category);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    books.add(mapResultSetToBook(rs));
                }
            }
        }
        return books;
    }

    @Override
    public List<Book> filterByAuthor(String author) throws SQLException {
        List<Book> books = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(FIND_BY_AUTHOR_SQL)) {
            ps.setString(1, author);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    books.add(mapResultSetToBook(rs));
                }
            }
        }
        return books;
    }

    @Override
    public boolean updateStatus(String isbn, boolean isActive) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_STATUS_SQL)) {
            ps.setBoolean(1, isActive);
            ps.setString(2, isbn);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean updateStock(String isbn, int change) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_STOCK_SQL)) {
            ps.setInt(1, change); // 'change' is positive for increment, negative for decrement
            ps.setString(2, isbn);
            return ps.executeUpdate() > 0;
        }
    }
}