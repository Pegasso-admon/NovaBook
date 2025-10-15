package view;

import controller.BookController;
import controller.LoanController;
import model.Book;
import model.Loan;
import util.AppLogger;
import util.CSVExporter;

import javax.swing.JOptionPane;
import java.util.List;

/**
 * View for data export operations (CSV files).
 */
public class ExportView {

    private final BookController bookController;
    private final LoanController loanController;

    public ExportView(BookController bookController, LoanController loanController) {
        this.bookController = bookController;
        this.loanController = loanController;
    }

    public void showExportMenu() {
        boolean running = true;

        while (running) {
            String menu = "=== Export Data ===\n\n" +
                    UIHelper.createMenu(
                            "Export All Books to CSV",
                            "Export Overdue Loans to CSV"
                    );

            String input = JOptionPane.showInputDialog(null, menu, "Export Menu", JOptionPane.PLAIN_MESSAGE);

            if (input == null || input.trim().equals("0")) {
                running = false;
                continue;
            }

            try {
                int option = Integer.parseInt(input.trim());

                switch (option) {
                    case 1: exportBooks(); break;
                    case 2: exportOverdueLoans(); break;
                    default:
                        JOptionPane.showMessageDialog(null, "Invalid option.", "Error", JOptionPane.WARNING_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void exportBooks() {
        try {
            String filename = JOptionPane.showInputDialog(
                    null,
                    "Enter filename (default: books_export.csv):",
                    "Export Books",
                    JOptionPane.PLAIN_MESSAGE
            );

            if (filename == null) return; // User cancelled

            if (filename.trim().isEmpty()) {
                filename = "books_export.csv";
            } else if (!filename.endsWith(".csv")) {
                filename += ".csv";
            }

            List<Book> books = bookController.getAllBooks();

            if (books.isEmpty()) {
                JOptionPane.showMessageDialog(
                        null,
                        "No books to export.",
                        "Export Books",
                        JOptionPane.INFORMATION_MESSAGE
                );
                return;
            }

            CSVExporter.exportBooks(books, filename);

            JOptionPane.showMessageDialog(
                    null,
                    "Books exported successfully!\n\n" +
                            "File: " + filename + "\n" +
                            "Total books: " + books.size(),
                    "Export Successful",
                    JOptionPane.INFORMATION_MESSAGE
            );

            AppLogger.logSuccess("Books Export", "Exported " + books.size() + " books to " + filename);

        } catch (Exception e) {
            AppLogger.logError("Failed to export books", e);
            JOptionPane.showMessageDialog(
                    null,
                    "Export failed: " + e.getMessage(),
                    "Export Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void exportOverdueLoans() {
        try {
            String filename = JOptionPane.showInputDialog(
                    null,
                    "Enter filename (default: prestamos_vencidos.csv):",
                    "Export Overdue Loans",
                    JOptionPane.PLAIN_MESSAGE
            );

            if (filename == null) return; // User cancelled

            if (filename.trim().isEmpty()) {
                filename = "overdue_loans.csv";
            } else if (!filename.endsWith(".csv")) {
                filename += ".csv";
            }

            List<Loan> loans = loanController.getOverdueLoans();

            if (loans.isEmpty()) {
                JOptionPane.showMessageDialog(
                        null,
                        "No overdue loans to export.",
                        "Export Overdue Loans",
                        JOptionPane.INFORMATION_MESSAGE
                );
                return;
            }

            CSVExporter.exportOverdueLoans(loans, filename);

            JOptionPane.showMessageDialog(
                    null,
                    "Overdue loans exported successfully!\n\n" +
                            "File: " + filename + "\n" +
                            "Total overdue: " + loans.size(),
                    "Export Successful",
                    JOptionPane.INFORMATION_MESSAGE
            );

            AppLogger.logSuccess("Overdue Loans Export", "Exported " + loans.size() + " overdue loans to " + filename);

        } catch (Exception e) {
            AppLogger.logError("Failed to export overdue loans", e);
            JOptionPane.showMessageDialog(
                    null,
                    "Export failed: " + e.getMessage(),
                    "Export Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}