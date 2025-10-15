package view;

import model.Book;
import model.Loan;
import model.Partner;
import model.User;
import java.util.List;

/**
 * Helper class to format data for display in JOptionPane dialogs.
 * Creates text-based tables with aligned columns.
 */
public class UIHelper {

    /**
     * Formats a list of books as a table string for display.
     * @param books List of books to format
     * @return Formatted table string
     */
    public static String formatBooksTable(List<Book> books) {
        if (books == null || books.isEmpty()) {
            return "No books found.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-15s %-30s %-20s %-15s %s\n",
                "ISBN", "Title", "Author", "Category", "Available/Total"));
        sb.append("=".repeat(100)).append("\n");

        for (Book book : books) {
            String status = book.isActive() ? "[ACTIVE]" : "[INACTIVE]";
            sb.append(String.format("%-15s %-30s %-20s %-15s %d/%d %s\n",
                    truncate(book.getIsbn(), 15),
                    truncate(book.getTitle(), 30),
                    truncate(book.getAuthor(), 20),
                    truncate(book.getCategory(), 15),
                    book.getAvailableCopies(),
                    book.getTotalCopies(),
                    status
            ));
        }

        return sb.toString();
    }

    /**
     * Formats a list of users as a table string for display.
     * @param users List of users to format
     * @return Formatted table string
     */
    public static String formatUsersTable(List<User> users) {
        if (users == null || users.isEmpty()) {
            return "No users found.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-5s %-20s %-15s %s\n", "ID", "Username", "Role", "Status"));
        sb.append("=".repeat(60)).append("\n");

        for (User user : users) {
            String status = user.isActive() ? "[ACTIVE]" : "[INACTIVE]";
            sb.append(String.format("%-5d %-20s %-15s %s\n",
                    user.getId(),
                    truncate(user.getUsername(), 20),
                    user.getRole(),
                    status
            ));
        }

        return sb.toString();
    }

    /**
     * Formats a list of partners as a table string for display.
     * @param partners List of partners to format
     * @return Formatted table string
     */
    public static String formatPartnersTable(List<Partner> partners) {
        if (partners == null || partners.isEmpty()) {
            return "No partners found.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-5s %-30s %-30s %s\n", "ID", "Name", "Email", "Status"));
        sb.append("=".repeat(80)).append("\n");

        for (Partner partner : partners) {
            String status = partner.isActive() ? "[ACTIVE]" : "[INACTIVE]";
            sb.append(String.format("%-5d %-30s %-30s %s\n",
                    partner.getId(),
                    truncate(partner.getName(), 30),
                    truncate(partner.getEmail(), 30),
                    status
            ));
        }

        return sb.toString();
    }

    /**
     * Formats a list of loans as a table string for display.
     * @param loans List of loans to format
     * @return Formatted table string
     */
    public static String formatLoansTable(List<Loan> loans) {
        if (loans == null || loans.isEmpty()) {
            return "No loans found.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-5s %-15s %-10s %-12s %-12s %s\n",
                "ID", "ISBN", "Partner", "Due Date", "Return Date", "Fine"));
        sb.append("=".repeat(80)).append("\n");

        for (Loan loan : loans) {
            String returnDate = loan.getReturnDate() != null ? loan.getReturnDate().toString() : "N/A";
            String fine = loan.getFine() != null ? "$" + loan.getFine().toString() : "$0.00";

            sb.append(String.format("%-5d %-15s %-10d %-12s %-12s %s\n",
                    loan.getId(),
                    truncate(loan.getBookIsbn(), 15),
                    loan.getPartnerId(),
                    loan.getDueDate().toString(),
                    returnDate,
                    fine
            ));
        }

        return sb.toString();
    }

    /**
     * Formats a single book's details for display.
     * @param book Book to format
     * @return Formatted string with book details
     */
    public static String formatBookDetails(Book book) {
        if (book == null) {
            return "Book not found.";
        }

        return String.format(
                "ISBN: %s\n" +
                        "Title: %s\n" +
                        "Author: %s\n" +
                        "Category: %s\n" +
                        "Total Copies: %d\n" +
                        "Available Copies: %d\n" +
                        "Reference Price: $%s\n" +
                        "Status: %s",
                book.getIsbn(),
                book.getTitle(),
                book.getAuthor(),
                book.getCategory(),
                book.getTotalCopies(),
                book.getAvailableCopies(),
                book.getReferencePrice().toString(),
                book.isActive() ? "ACTIVE" : "INACTIVE"
        );
    }

    /**
     * Truncates a string to a maximum length and adds "..." if needed.
     * @param str String to truncate
     * @param maxLength Maximum length
     * @return Truncated string
     */
    private static String truncate(String str, int maxLength) {
        if (str == null) {
            return "";
        }
        if (str.length() <= maxLength) {
            return str;
        }
        return str.substring(0, maxLength - 3) + "...";
    }

    /**
     * Creates a simple menu option string.
     * @param options Array of menu options
     * @return Formatted menu string
     */
    public static String createMenu(String... options) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < options.length; i++) {
            sb.append((i + 1)).append(". ").append(options[i]).append("\n");
        }
        sb.append("0. Exit/Back\n");
        return sb.toString();
    }
}