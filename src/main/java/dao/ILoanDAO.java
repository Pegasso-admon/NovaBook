package dao;

import model.Loan;
import java.sql.SQLException;
import java.util.List;

public interface ILoanDAO {

    // Transaction 1: Register New Loan (Commit/Rollback managed by Service)
    Loan insert(Loan loan) throws SQLException;
    Loan findById(int id) throws SQLException;

    // Transaction 2: Process Return (Update return date, fine, and status)
    boolean updateForReturn(Loan loan) throws SQLException;

    // Reporting requirement (For CSV export)
    List<Loan> findOverdueLoans() throws SQLException;

    // Listing active and returned loans
    List<Loan> findActiveLoans() throws SQLException;
}