package service;

import exception.BusinessException;
import exception.InvalidPartnerException;
import exception.InsufficientStockException;
import model.Loan;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public interface ILoanService {

    // Transaction 1: Register a new loan. Requires cross-DAO operations (Partner, Book, Loan).
    Loan registerLoan(Loan loan) throws BusinessException, SQLException, InvalidPartnerException, InsufficientStockException;

    // Transaction 2: Process the return of a book.
    boolean processReturn(int loanId, Date returnDate) throws BusinessException, SQLException;

    // Business Logic: Calculates the fine for a late return.
    BigDecimal calculateFine(Date dueDate, Date returnDate);

    // Reporting methods
    List<Loan> findActiveLoans() throws SQLException;
    List<Loan> findOverdueLoans() throws SQLException;
}