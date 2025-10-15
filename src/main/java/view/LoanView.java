package view;

import controller.LoanController;
import controller.BookController;
import controller.PartnerController;
import model.Loan;
import model.Book;
import model.Partner;
import util.AppLogger;
import util.ConfigLoader;

import javax.swing.JOptionPane;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

/**
 * View for Loan management operations.
 */
public class LoanView {

    private final LoanController loanController;
    private final BookController bookController;
    private final PartnerController partnerController;

    public LoanView(LoanController loanController, BookController bookController, PartnerController partnerController) {
        this.loanController = loanController;
        this.bookController = bookController;
        this.partnerController = partnerController;
    }

    public void showLoanMenu() {
        boolean running = true;

        while (running) {
            String menu = "=== Loans Management ===\n\n" +
                    UIHelper.createMenu(
                            "Register New Loan",
                            "Process Book Return",
                            "View Active Loans",
                            "View Overdue Loans"
                    );

            String input = JOptionPane.showInputDialog(null, menu, "Loans Menu", JOptionPane.PLAIN_MESSAGE);

            if (input == null || input.trim().equals("0")) {
                running = false;
                continue;
            }

            try {
                int option = Integer.parseInt(input.trim());

                switch (option) {
                    case 1: registerLoan(); break;
                    case 2: processReturn(); break;
                    case 3: viewActiveLoans(); break;
                    case 4: viewOverdueLoans(); break;
                    default:
                        JOptionPane.showMessageDialog(null, "Invalid option.", "Error", JOptionPane.WARNING_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void registerLoan() {
        try {
            // Step 1: Get Book ISBN
            String isbn = JOptionPane.showInputDialog(null, "Enter Book ISBN:", "Register Loan", JOptionPane.PLAIN_MESSAGE);
            if (isbn == null || isbn.trim().isEmpty()) return;

            // Verify book exists and is available
            Book book = bookController.findBookByIsbn(isbn.trim());
            if (book == null) {
                JOptionPane.showMessageDialog(null, "Book not found with ISBN: " + isbn, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (book.getAvailableCopies() < 1) {
                JOptionPane.showMessageDialog(null, "Book is out of stock!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Step 2: Get Partner ID
            String partnerIdStr = JOptionPane.showInputDialog(null, "Enter Partner ID:", "Register Loan", JOptionPane.PLAIN_MESSAGE);
            if (partnerIdStr == null || partnerIdStr.trim().isEmpty()) return;

            int partnerId = Integer.parseInt(partnerIdStr.trim());

            // Verify partner exists
            Partner partner = partnerController.findPartnerById(partnerId);
            if (partner == null) {
                JOptionPane.showMessageDialog(null, "Partner not found with ID: " + partnerId, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!partner.isActive()) {
                JOptionPane.showMessageDialog(null, "Partner is inactive and cannot borrow books.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Step 3: Calculate due date (from config)
            int loanDays = ConfigLoader.getIntProperty("loan.days", 7);
            LocalDate today = LocalDate.now();
            LocalDate dueLocalDate = today.plusDays(loanDays);
            Date dueDate = Date.valueOf(dueLocalDate);

            // Step 4: Create and register loan
            Loan loan = new Loan();
            loan.setBookIsbn(isbn.trim());
            loan.setPartnerId(partnerId);
            loan.setDueDate(dueDate);

            Loan registered = loanController.registerLoan(loan);

            JOptionPane.showMessageDialog(
                    null,
                    "Loan registered successfully!\n\n" +
                            "Loan ID: " + registered.getId() + "\n" +
                            "Book: " + book.getTitle() + "\n" +
                            "Partner: " + partner.getName() + "\n" +
                            "Due Date: " + registered.getDueDate(),
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid number format.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            AppLogger.logError("Failed to register loan", e);
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Loan Registration Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void processReturn() {
        try {
            String loanIdStr = JOptionPane.showInputDialog(null, "Enter Loan ID:", "Process Return", JOptionPane.PLAIN_MESSAGE);
            if (loanIdStr == null || loanIdStr.trim().isEmpty()) return;

            int loanId = Integer.parseInt(loanIdStr.trim());

            // Use today as return date
            LocalDate today = LocalDate.now();
            Date returnDate = Date.valueOf(today);

            boolean processed = loanController.processReturn(loanId, returnDate);

            if (processed) {
                JOptionPane.showMessageDialog(
                        null,
                        "Return processed successfully!\n\nLoan ID: " + loanId + "\nReturn Date: " + returnDate,
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );
            } else {
                JOptionPane.showMessageDialog(null, "Failed to process return.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid number format.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            AppLogger.logError("Failed to process return", e);
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Return Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void viewActiveLoans() {
        try {
            List<Loan> loans = loanController.getActiveLoans();

            if (loans.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No active loans found.", "Active Loans", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            String table = UIHelper.formatLoansTable(loans);
            JOptionPane.showMessageDialog(null, table, "Active Loans (" + loans.size() + " total)", JOptionPane.PLAIN_MESSAGE);

        } catch (Exception e) {
            AppLogger.logError("Failed to view active loans", e);
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void viewOverdueLoans() {
        try {
            List<Loan> loans = loanController.getOverdueLoans();

            if (loans.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No overdue loans found.", "Overdue Loans", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            String table = UIHelper.formatLoansTable(loans);
            JOptionPane.showMessageDialog(null, table, "Overdue Loans (" + loans.size() + " total)", JOptionPane.WARNING_MESSAGE);

        } catch (Exception e) {
            AppLogger.logError("Failed to view overdue loans", e);
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}