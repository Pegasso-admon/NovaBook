package service.impl;

import dao.IBookDAO;
import dao.ILoanDAO;
import dao.IPartnerDAO;
import exception.BusinessException;
import model.Book;
import model.Loan;
import service.ILoanService;
import util.DBConnection; // Required to get Connection
import java.sql.Connection; // Required for transaction management
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.time.temporal.ChronoUnit;
import java.time.LocalDate;
import java.util.List;

public class LoanServiceImpl implements ILoanService {

    private final ILoanDAO loanDAO;
    private final IBookDAO bookDAO;
    private final IPartnerDAO partnerDAO;

    private static final BigDecimal FINE_PER_DAY = new BigDecimal("0.50");

    public LoanServiceImpl(ILoanDAO loanDAO, IBookDAO bookDAO, IPartnerDAO partnerDAO) {
        this.loanDAO = loanDAO;
        this.bookDAO = bookDAO;
        this.partnerDAO = partnerDAO;
    }

    @Override
    public Loan registerLoan(Loan loan) throws BusinessException, SQLException {

        // Read-only Business Validations
        if (!partnerDAO.isActive(loan.getPartnerId())) {
            throw new BusinessException("Partner is inactive and cannot loan books.");
        }

        Book book = bookDAO.findByIsbn(loan.getBookIsbn());
        if (book == null || book.getAvailableCopies() < 1) {
            throw new BusinessException("Book is out of stock or not found.");
        }

        // --- CRITICAL FIX: JDBC Transaction Management ---
        Connection conn = null;
        Loan newLoan = null;

        try {
            // 1. Get connection and disable auto-commit
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            // 2. Insert Loan: Pass the controlled connection
            newLoan = loanDAO.insert(loan, conn);

            // 3. Update Stock (Decrement -1): Pass the controlled connection
            boolean stockUpdated = bookDAO.updateStock(loan.getBookIsbn(), -1, conn);

            if (!stockUpdated) {
                throw new BusinessException("Failed to update book stock. Loan failed.");
            }

            conn.commit(); // Commit if both DAO operations succeed
            return newLoan;

        } catch (Exception e) {
            if (conn != null) {
                conn.rollback(); // Rollback on any failure
            }
            // Re-throw the original exception type
            if (e instanceof BusinessException) throw (BusinessException) e;
            if (e instanceof SQLException) throw (SQLException) e;
            throw new RuntimeException("Unexpected error during loan registration.", e);
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true); // Restore default setting
                conn.close(); // Close the connection
            }
        }
    }

    @Override
    public boolean processReturn(int loanId, Date returnDate) throws BusinessException, SQLException {
        Loan loan = loanDAO.findById(loanId);

        // Return validations
        if (loan == null) {
            throw new BusinessException("Loan ID " + loanId + " not found.");
        }
        if (loan.isReturned()) {
            throw new BusinessException("Book is already returned.");
        }

        // Calculate fine
        BigDecimal fineAmount = calculateFine(loan.getDueDate(), returnDate);
        loan.setReturnDate(returnDate);
        loan.setFine(fineAmount);

        // --- CRITICAL FIX: JDBC Transaction Management ---
        Connection conn = null;

        try {
            // 1. Get connection and disable auto-commit
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            // 2. Update Loan for return: Pass the controlled connection
            boolean loanUpdated = loanDAO.updateForReturn(loan, conn);

            // 3. Update Stock (Increment +1): Pass the controlled connection
            boolean stockUpdated = bookDAO.updateStock(loan.getBookIsbn(), 1, conn);

            if (!loanUpdated || !stockUpdated) {
                throw new BusinessException("Return process failed. Data consistency issue.");
            }

            conn.commit(); // Commit if both DAO operations succeed
            return true;

        } catch (Exception e) {
            if (conn != null) {
                conn.rollback(); // Rollback on any failure
            }
            // Re-throw the original exception type
            if (e instanceof BusinessException) throw (BusinessException) e;
            if (e instanceof SQLException) throw (SQLException) e;
            throw new RuntimeException("Unexpected error during return process.", e);
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true); // Restore default setting
                conn.close(); // Close the connection
            }
        }
    }

    @Override
    public BigDecimal calculateFine(Date dueDate, Date returnDate) {
        // Business Logic: Calculate days overdue and multiply by the fine rate.

        LocalDate due = dueDate.toLocalDate();
        LocalDate ret = returnDate.toLocalDate();

        if (ret.isBefore(due) || ret.isEqual(due)) {
            return BigDecimal.ZERO;
        }

        long daysOverdue = ChronoUnit.DAYS.between(due, ret);

        // Fine calculation: days * FINE_PER_DAY
        return FINE_PER_DAY.multiply(new BigDecimal(daysOverdue));
    }

    @Override
    public List<Loan> findActiveLoans() throws SQLException {
        // Simple delegation (read-only)
        return loanDAO.findActiveLoans();
    }

    @Override
    public List<Loan> findOverdueLoans() throws SQLException {
        // Simple delegation (read-only)
        return loanDAO.findOverdueLoans();
    }
}