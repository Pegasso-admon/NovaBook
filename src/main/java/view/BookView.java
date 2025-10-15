package view;

import controller.BookController;
import model.Book;
import util.AppLogger;

import javax.swing.JOptionPane;
import java.math.BigDecimal;
import java.util.List;

/**
 * View for Book management operations using JOptionPane.
 */
public class BookView {

    private final BookController bookController;

    public BookView(BookController bookController) {
        this.bookController = bookController;
    }

    public void showBookMenu() {
        boolean running = true;

        while (running) {
            String menu = "=== Books Management ===\n\n" +
                    UIHelper.createMenu(
                            "Register New Book",
                            "List All Books",
                            "Update Book",
                            "Find Book by ISBN",
                            "Filter by Category",
                            "Filter by Author",
                            "Deactivate Book"
                    );

            String input = JOptionPane.showInputDialog(null, menu, "Books Menu", JOptionPane.PLAIN_MESSAGE);

            if (input == null || input.trim().equals("0")) {
                running = false;
                continue;
            }

            try {
                int option = Integer.parseInt(input.trim());

                switch (option) {
                    case 1: registerBook(); break;
                    case 2: listAllBooks(); break;
                    case 3: updateBook(); break;
                    case 4: findBookByIsbn(); break;
                    case 5: filterByCategory(); break;
                    case 6: filterByAuthor(); break;
                    case 7: deactivateBook(); break;
                    default:
                        JOptionPane.showMessageDialog(null, "Invalid option.", "Error", JOptionPane.WARNING_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void registerBook() {
        try {
            String isbn = JOptionPane.showInputDialog(null, "Enter ISBN:", "Register Book", JOptionPane.PLAIN_MESSAGE);
            if (isbn == null || isbn.trim().isEmpty()) return;

            String title = JOptionPane.showInputDialog(null, "Enter Title:", "Register Book", JOptionPane.PLAIN_MESSAGE);
            if (title == null || title.trim().isEmpty()) return;

            String author = JOptionPane.showInputDialog(null, "Enter Author:", "Register Book", JOptionPane.PLAIN_MESSAGE);
            if (author == null || author.trim().isEmpty()) return;

            String category = JOptionPane.showInputDialog(null, "Enter Category:", "Register Book", JOptionPane.PLAIN_MESSAGE);
            if (category == null || category.trim().isEmpty()) return;

            String priceStr = JOptionPane.showInputDialog(null, "Enter Reference Price:", "Register Book", JOptionPane.PLAIN_MESSAGE);
            if (priceStr == null || priceStr.trim().isEmpty()) return;

            String totalStr = JOptionPane.showInputDialog(null, "Enter Total Copies:", "Register Book", JOptionPane.PLAIN_MESSAGE);
            if (totalStr == null || totalStr.trim().isEmpty()) return;

            String availableStr = JOptionPane.showInputDialog(null, "Enter Available Copies:", "Register Book", JOptionPane.PLAIN_MESSAGE);
            if (availableStr == null || availableStr.trim().isEmpty()) return;

            Book book = new Book();
            book.setIsbn(isbn.trim());
            book.setTitle(title.trim());
            book.setAuthor(author.trim());
            book.setCategory(category.trim());
            book.setReferencePrice(new BigDecimal(priceStr.trim()));
            book.setTotalCopies(Integer.parseInt(totalStr.trim()));
            book.setAvailableCopies(Integer.parseInt(availableStr.trim()));
            book.setActive(true);

            Book registered = bookController.registerBook(book);

            JOptionPane.showMessageDialog(
                    null,
                    "Book registered successfully!\n\n" + UIHelper.formatBookDetails(registered),
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid number format.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            AppLogger.logError("Failed to register book", e);
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Registration Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void listAllBooks() {
        try {
            List<Book> books = bookController.getAllBooks();

            if (books.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No books found.", "Books List", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            String table = UIHelper.formatBooksTable(books);
            JOptionPane.showMessageDialog(null, table, "Books List (" + books.size() + " total)", JOptionPane.PLAIN_MESSAGE);

        } catch (Exception e) {
            AppLogger.logError("Failed to list books", e);
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateBook() {
        try {
            String isbn = JOptionPane.showInputDialog(null, "Enter ISBN to update:", "Update Book", JOptionPane.PLAIN_MESSAGE);
            if (isbn == null || isbn.trim().isEmpty()) return;

            Book book = bookController.findBookByIsbn(isbn.trim());

            if (book == null) {
                JOptionPane.showMessageDialog(null, "Book not found with ISBN: " + isbn, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String title = JOptionPane.showInputDialog(null, "New Title [" + book.getTitle() + "]:", "Update Book", JOptionPane.PLAIN_MESSAGE);
            if (title != null && !title.trim().isEmpty()) {
                book.setTitle(title.trim());
            }

            String author = JOptionPane.showInputDialog(null, "New Author [" + book.getAuthor() + "]:", "Update Book", JOptionPane.PLAIN_MESSAGE);
            if (author != null && !author.trim().isEmpty()) {
                book.setAuthor(author.trim());
            }

            String category = JOptionPane.showInputDialog(null, "New Category [" + book.getCategory() + "]:", "Update Book", JOptionPane.PLAIN_MESSAGE);
            if (category != null && !category.trim().isEmpty()) {
                book.setCategory(category.trim());
            }

            String totalStr = JOptionPane.showInputDialog(null, "New Total Copies [" + book.getTotalCopies() + "]:", "Update Book", JOptionPane.PLAIN_MESSAGE);
            if (totalStr != null && !totalStr.trim().isEmpty()) {
                book.setTotalCopies(Integer.parseInt(totalStr.trim()));
            }

            String availableStr = JOptionPane.showInputDialog(null, "New Available Copies [" + book.getAvailableCopies() + "]:", "Update Book", JOptionPane.PLAIN_MESSAGE);
            if (availableStr != null && !availableStr.trim().isEmpty()) {
                book.setAvailableCopies(Integer.parseInt(availableStr.trim()));
            }

            bookController.updateBook(book);

            JOptionPane.showMessageDialog(null, "Book updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            AppLogger.logError("Failed to update book", e);
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Update Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void findBookByIsbn() {
        try {
            String isbn = JOptionPane.showInputDialog(null, "Enter ISBN:", "Find Book", JOptionPane.PLAIN_MESSAGE);
            if (isbn == null || isbn.trim().isEmpty()) return;

            Book book = bookController.findBookByIsbn(isbn.trim());

            if (book == null) {
                JOptionPane.showMessageDialog(null, "Book not found with ISBN: " + isbn, "Not Found", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            String details = UIHelper.formatBookDetails(book);
            JOptionPane.showMessageDialog(null, details, "Book Details", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            AppLogger.logError("Failed to find book", e);
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void filterByCategory() {
        try {
            String category = JOptionPane.showInputDialog(null, "Enter Category:", "Filter Books", JOptionPane.PLAIN_MESSAGE);
            if (category == null || category.trim().isEmpty()) return;

            List<Book> books = bookController.filterBooksByCategory(category.trim());

            if (books.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No books found in category: " + category, "Filter Results", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            String table = UIHelper.formatBooksTable(books);
            JOptionPane.showMessageDialog(null, table, "Books in Category: " + category, JOptionPane.PLAIN_MESSAGE);

        } catch (Exception e) {
            AppLogger.logError("Failed to filter by category", e);
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void filterByAuthor() {
        try {
            String author = JOptionPane.showInputDialog(null, "Enter Author:", "Filter Books", JOptionPane.PLAIN_MESSAGE);
            if (author == null || author.trim().isEmpty()) return;

            List<Book> books = bookController.filterBooksByCategory(author.trim());

            if (books.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No books found by author: " + author, "Filter Results", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            String table = UIHelper.formatBooksTable(books);
            JOptionPane.showMessageDialog(null, table, "Books by: " + author, JOptionPane.PLAIN_MESSAGE);

        } catch (Exception e) {
            AppLogger.logError("Failed to filter by author", e);
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deactivateBook() {
        try {
            String isbn = JOptionPane.showInputDialog(null, "Enter ISBN to deactivate:", "Deactivate Book", JOptionPane.PLAIN_MESSAGE);
            if (isbn == null || isbn.trim().isEmpty()) return;

            int confirm = JOptionPane.showConfirmDialog(
                    null,
                    "Are you sure you want to deactivate book with ISBN: " + isbn + "?",
                    "Confirm Deactivation",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm != JOptionPane.YES_OPTION) return;

            boolean deactivated = bookController.deactivateBook(isbn.trim());

            if (deactivated) {
                JOptionPane.showMessageDialog(null, "Book deactivated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Failed to deactivate book.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {
            AppLogger.logError("Failed to deactivate book", e);
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}