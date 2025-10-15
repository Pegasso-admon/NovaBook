package service.impl;

import dao.IBookDAO;
import dao.ILoanDAO;
import dao.IPartnerDAO;
import exception.BusinessException;
import exception.InsufficientStockException;
import exception.InvalidPartnerException;
import model.Book;
import model.Loan;
import service.ILoanService;
import util.DBConnection;
import java.sql.Connection;
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
    public Loan registerLoan(Loan loan) throws BusinessException, SQLException, InvalidPartnerException, InsufficientStockException {
        Connection conn = null;
        Loan newLoan = null;

        try {
            // Validations BEFORE transaction
            if (!partnerDAO.isActive(loan.getPartnerId())) {
                throw new InvalidPartnerException(loan.getPartnerId());
            }

            Book book = bookDAO.findByIsbn(loan.getBookIsbn());
            if (book == null) {
                throw new InsufficientStockException(loan.getBookIsbn(), 0);
            }
            if (book.getAvailableCopies() < 1) {
                throw new InsufficientStockException(loan.getBookIsbn(), book.getAvailableCopies());
            }

            // Start transaction
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            // Insert Loan
            newLoan = loanDAO.insert(loan, conn);

            // Update Stock (Decrement -1)
            boolean stockUpdated = bookDAO.updateStock(loan.getBookIsbn(), -1, conn);

            if (!stockUpdated) {
                throw new BusinessException("Failed to update book stock. Loan failed.");
            }

            conn.commit();
            return newLoan;

        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            if (e instanceof BusinessException) throw (BusinessException) e;
            if (e instanceof InvalidPartnerException) throw (InvalidPartnerException) e;
            if (e instanceof InsufficientStockException) throw (InsufficientStockException) e;
            if (e instanceof SQLException) throw (SQLException) e;
            throw new RuntimeException("Unexpected error during loan registration.", e);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    @Override
    public boolean processReturn(int loanId, Date returnDate) throws BusinessException, SQLException {
        Connection conn = null;

        try {
            Loan loan = loanDAO.findById(loanId);

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

            // Start transaction
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            // Update Loan for return
            boolean loanUpdated = loanDAO.updateForReturn(loan, conn);

            // Update Stock (Increment +1)
            boolean stockUpdated = bookDAO.updateStock(loan.getBookIsbn(), 1, conn);

            if (!loanUpdated || !stockUpdated) {
                throw new BusinessException("Return process failed. Data consistency issue.");
            }

            conn.commit();
            return true;

        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            if (e instanceof BusinessException) throw (BusinessException) e;
            if (e instanceof SQLException) throw (SQLException) e;
            throw new RuntimeException("Unexpected error during return process.", e);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    @Override
    public BigDecimal calculateFine(Date dueDate, Date returnDate) {
        LocalDate due = dueDate.toLocalDate();
        LocalDate ret = returnDate.toLocalDate();

        if (ret.isBefore(due) || ret.isEqual(due)) {
            return BigDecimal.ZERO;
        }

        long daysOverdue = ChronoUnit.DAYS.between(due, ret);
        return FINE_PER_DAY.multiply(new BigDecimal(daysOverdue));
    }

    @Override
    public List<Loan> findActiveLoans() throws SQLException {
        return loanDAO.findActiveLoans();
    }

    @Override
    public List<Loan> findOverdueLoans() throws SQLException {
        return loanDAO.findOverdueLoans();
    }
}