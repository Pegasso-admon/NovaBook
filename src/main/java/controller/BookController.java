package controller;

import service.IBookService;
import model.Book;
import exception.BusinessException;

import java.sql.SQLException;
import java.util.List;

/**
 * Controller class for managing Book-related CRUD and filtering.
 */
public class BookController {

    private final IBookService bookService;

    /**
     * Dependency Injection (via constructor).
     */
    public BookController(IBookService bookService) {
        this.bookService = bookService;
    }

    /**
     * Registers a new book.
     * @param book The Book object to save.
     * @return The registered Book object.
     * @throws BusinessException If ISBN exists or stock is invalid.
     * @throws SQLException Database access error.
     */
    public Book registerBook(Book book) throws BusinessException, SQLException {
        return bookService.register(book);
    }

    /**
     * Updates an existing book (details, stock, total copies).
     * @param book The Book object with updated information.
     * @return True if the update was successful.
     * @throws BusinessException If update violates business rules (e.g., stock constraint).
     * @throws SQLException Database access error.
     */
    public boolean updateBook(Book book) throws BusinessException, SQLException {
        return bookService.update(book);
    }

    /**
     * Finds a book by its unique ISBN.
     * @param isbn The ISBN to search for.
     * @return The Book object or null.
     * @throws SQLException Database access error.
     */
    public Book findBookByIsbn(String isbn) throws SQLException {
        return bookService.findByIsbn(isbn);
    }

    /**
     * Deactivates a book (soft delete).
     * @param isbn The ISBN of the book to deactivate.
     * @return True if the status was updated.
     * @throws SQLException Database access error.
     */
    public boolean deactivateBook(String isbn) throws SQLException {
        return bookService.updateStatus(isbn, false);
    }

    // --- Filtering and Listing Endpoints ---

    /**
     * Retrieves all books.
     * @return List of all Book objects.
     * @throws SQLException Database access error.
     */
    public List<Book> getAllBooks() throws SQLException {
        return bookService.findAll();
    }

    /**
     * Filters books by category.
     * @param category The category name.
     * @return List of books in that category.
     * @throws SQLException Database access error.
     */
    public List<Book> filterBooksByCategory(String category) throws SQLException {
        return bookService.filterByCategory(category);
    }
}