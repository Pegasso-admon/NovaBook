package controller;

import service.ILoanService;
import service.impl.LoanServiceImpl;
import dao.LoanDAOImpl;
import model.Loan;
import exception.BusinessException;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

/**
 * Controller class for managing Loan-related operations.
 * Handles requests for registering loans, processing returns, and generating reports.
 */
public class LoanController {

    private final ILoanService loanService;

    /**
     * Dependency Injection (via constructor).
     */
    public LoanController(ILoanService loanService) {
        this.loanService = loanService;
    }

    /**
     * 1. Registers a new loan (Transactional: Loan insert + Book stock update).
     * @param loan The Loan object containing partner ID and book ISBN.
     * @return The registered Loan object with its generated ID.
     * @throws Exception Business or SQL error.
     */
    public Loan registerLoan(Loan loan) throws Exception {
        // The service layer handles all business logic and the JDBC transaction.
        return loanService.registerLoan(loan);
    }

    /**
     * 2. Processes the return of a book (Transactional: Loan update + Book stock update).
     * @param loanId The ID of the loan to close.
     * @param returnDate The actual return date.
     * @return True if the return was successful.
     * @throws Exception Business or SQL error (including overdue fines).
     */
    public boolean processReturn(int loanId, Date returnDate) throws Exception {
        // The service layer handles transaction, fine calculation, and stock update.
        return loanService.processReturn(loanId, returnDate);
    }

    // --- Reporting Endpoints ---

    /**
     * Retrieves all currently active loans.
     * @return List of active Loan objects.
     * @throws SQLException Database access error.
     */
    public List<Loan> getActiveLoans() throws SQLException {
        return loanService.findActiveLoans();
    }

    /**
     * Retrieves all overdue loans (past due date and not returned).
     * @return List of overdue Loan objects.
     * @throws SQLException Database access error.
     */
    public List<Loan> getOverdueLoans() throws SQLException {
        return loanService.findOverdueLoans();
    }
}