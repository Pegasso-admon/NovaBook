package controller;

import service.IBookService;
import model.Book;
import exception.BusinessException;
import util.AppLogger;

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
    public Book registerBook(Book book) throws Exception {
        AppLogger.logHttpRequest("POST", "/NovaBook/books", "Registering book: " + book.getIsbn());
        try {
            Book registered = bookService.register(book);
            AppLogger.logSuccess("Book Registration", "ISBN: " + registered.getIsbn() + " - " + registered.getTitle());
            return registered;
        } catch (Exception e) {
            AppLogger.logError("POST /NovaBook/books - Failed to register book: " + book.getIsbn(), e);
            throw e;
        }
    }

    /**
     * Updates an existing book (details, stock, total copies).
     * @param book The Book object with updated information.
     * @return True if the update was successful.
     * @throws BusinessException If update violates business rules (e.g., stock constraint).
     * @throws SQLException Database access error.
     */
    public boolean updateBook(Book book) throws Exception {
        AppLogger.logHttpRequest("PATCH", "/NovaBook/books/" + book.getIsbn(), "Updating book");
        try {
            boolean updated = bookService.update(book);
            if (updated) {
                AppLogger.logSuccess("Book Update", "ISBN: " + book.getIsbn());
            }
            return updated;
        } catch (Exception e) {
            AppLogger.logError("PATCH /NovaBook/books/" + book.getIsbn() + " - Failed", e);
            throw e;
        }
    }

    /**
     * Finds a book by its unique ISBN.
     * @param isbn The ISBN to search for.
     * @return The Book object or null.
     * @throws SQLException Database access error.
     */
    public Book findBookByIsbn(String isbn) throws SQLException {
        AppLogger.logHttpRequest("GET", "/NovaBook/books/" + isbn, "Fetching book");
        Book book = bookService.findByIsbn(isbn);
        if (book != null) {
            AppLogger.logInfo("Book found: " + book.getTitle());
        } else {
            AppLogger.logWarning("Book not found with ISBN: " + isbn);
        }
        return book;
    }

    /**
     * Deactivates a book (soft delete).
     * @param isbn The ISBN of the book to deactivate.
     * @return True if the status was updated.
     * @throws SQLException Database access error.
     */
    public boolean deactivateBook(String isbn) throws SQLException {
        AppLogger.logHttpRequest("DELETE", "/NovaBook/books/" + isbn, "Deactivating book");
        try {
            boolean deactivated = bookService.updateStatus(isbn, false);
            if (deactivated) {
                AppLogger.logSuccess("Book Deactivation", "ISBN: " + isbn);
            }
            return deactivated;
        } catch (Exception e) {
            AppLogger.logError("DELETE /NovaBook/books/" + isbn + " - Failed", e);
            throw e;
        }
    }

    // --- Filtering and Listing Endpoints ---

    /**
     * Retrieves all books.
     * @return List of all Book objects.
     * @throws SQLException Database access error.
     */
    public List<Book> getAllBooks() throws SQLException {
        AppLogger.logHttpRequest("GET", "/NovaBook/books", "Fetching all books");
        List<Book> books = bookService.findAll();
        AppLogger.logInfo("Retrieved " + books.size() + " books");
        return books;
    }

    /**
     * Filters books by category.
     * @param category The category name.
     * @return List of books in that category.
     * @throws SQLException Database access error.
     */
    public List<Book> filterBooksByCategory(String category) throws SQLException {
        AppLogger.logHttpRequest("GET", "/NovaBook/books?category=" + category, "Filtering by category");
        List<Book> books = bookService.filterByCategory(category);
        AppLogger.logInfo("Found " + books.size() + " books in category: " + category);
        return books;
    }
}