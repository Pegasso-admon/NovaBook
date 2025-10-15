package util;

import model.Book;
import model.Loan;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CSVExporter {

    /**
     * Exports a list of books to a CSV file.
     * @param books List of books to export
     * @param filePath Path where the CSV will be saved
     * @throws IOException If file writing fails
     */
    public static void exportBooks(List<Book> books, String filePath) throws IOException {
        try (FileWriter writer = new FileWriter(filePath)) {
            // CSV Header
            writer.append("ISBN,Title,Author,Category,Total Copies,Available Copies,Reference Price,Active\n");

            // CSV Data
            for (Book book : books) {
                writer.append(book.getIsbn()).append(",");
                writer.append(escapeCsv(book.getTitle())).append(",");
                writer.append(escapeCsv(book.getAuthor())).append(",");
                writer.append(escapeCsv(book.getCategory())).append(",");
                writer.append(String.valueOf(book.getTotalCopies())).append(",");
                writer.append(String.valueOf(book.getAvailableCopies())).append(",");
                writer.append(book.getReferencePrice().toString()).append(",");
                writer.append(book.isActive() ? "ACTIVE" : "INACTIVE").append("\n");
            }
        }
    }

    /**
     * Exports a list of overdue loans to a CSV file.
     * @param loans List of overdue loans to export
     * @param filePath Path where the CSV will be saved
     * @throws IOException If file writing fails
     */
    public static void exportOverdueLoans(List<Loan> loans, String filePath) throws IOException {
        try (FileWriter writer = new FileWriter(filePath)) {
            // CSV Header
            writer.append("Loan ID,Book ISBN,Partner ID,Loan Date,Due Date,Days Overdue,Fine\n");

            // CSV Data
            for (Loan loan : loans) {
                long daysOverdue = java.time.temporal.ChronoUnit.DAYS.between(
                        loan.getDueDate().toLocalDate(),
                        java.time.LocalDate.now()
                );

                writer.append(String.valueOf(loan.getId())).append(",");
                writer.append(loan.getBookIsbn()).append(",");
                writer.append(String.valueOf(loan.getPartnerId())).append(",");
                writer.append(loan.getLoanDate().toString()).append(",");
                writer.append(loan.getDueDate().toString()).append(",");
                writer.append(String.valueOf(daysOverdue)).append(",");
                writer.append(loan.getFine() != null ? loan.getFine().toString() : "0.00").append("\n");
            }
        }
    }

    /**
     * Escapes special characters in CSV fields (commas, quotes, newlines).
     * @param value The string to escape
     * @return Escaped string
     */
    private static String escapeCsv(String value) {
        if (value == null) {
            return "";
        }
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}