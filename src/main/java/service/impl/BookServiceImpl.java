package service.impl;

import dao.IBookDAO;
import exception.BusinessException;
import exception.ExistingISBNException;
import model.Book;
import service.IBookService;
import java.sql.SQLException;
import java.util.List;

public class BookServiceImpl implements IBookService {

    private final IBookDAO bookDAO;

    public BookServiceImpl(IBookDAO bookDAO) {
        this.bookDAO = bookDAO;
    }

    @Override
    public Book register(Book book) throws BusinessException, SQLException, ExistingISBNException {
        // Business Logic: Check for unique ISBN
        if (bookDAO.findByIsbn(book.getIsbn()) != null) {
            throw new ExistingISBNException(book.getIsbn());
        }
        // Validation: Available copies cannot exceed total copies
        if (book.getAvailableCopies() > book.getTotalCopies()) {
            throw new BusinessException("Available copies cannot exceed total copies.");
        }
        return bookDAO.insert(book);
    }

    @Override
    public boolean update(Book book) throws BusinessException, SQLException {
        // 1. Retrieve the existing book data to check current loan status
        Book existingBook = bookDAO.findByIsbn(book.getIsbn());
        if (existingBook == null) {
            throw new BusinessException("Cannot update: Book with ISBN " + book.getIsbn() + " not found.");
        }

        // 2. Calculate currently loaned copies
        // Loaned copies = Total copies (old) - Available copies (old)
        int currentlyLoaned = existingBook.getTotalCopies() - existingBook.getAvailableCopies();
        int newAvailable = book.getAvailableCopies();
        int newTotal = book.getTotalCopies();

        // Business Validation 1: New total copies must be enough for currently loaned books.
        if (newTotal < currentlyLoaned) {
            throw new BusinessException("Cannot reduce total copies below " + currentlyLoaned + " (currently loaned out).");
        }

        // Business Validation 2: New available copies must be enough for currently loaned books.
        // This prevents creating inconsistency if TotalCopies is increased but Available is not.
        if (newAvailable < currentlyLoaned) {
            throw new BusinessException("Available copies cannot be less than " + currentlyLoaned + " copies currently loaned out.");
        }

        // Business Validation 3: Available copies must not exceed the new total copies.
        if (newAvailable > newTotal) {
            throw new BusinessException("Available copies must not exceed total copies (" + newTotal + ").");
        }

        // 3. Delegate to DAO if all validations pass
        return bookDAO.update(book);
    }

    @Override
    public Book findByIsbn(String isbn) throws SQLException {
        return bookDAO.findByIsbn(isbn);
    }

    @Override
    public List<Book> findAll() throws SQLException {
        return bookDAO.findAll();
    }

    @Override
    public List<Book> filterByCategory(String category) throws SQLException {
        return bookDAO.filterByCategory(category);
    }

    @Override
    public List<Book> filterByAuthor(String author) throws SQLException {
        return bookDAO.filterByAuthor(author);
    }

    @Override
    public boolean updateStatus(String isbn, boolean isActive) throws SQLException {
        return bookDAO.updateStatus(isbn, isActive);
    }
}