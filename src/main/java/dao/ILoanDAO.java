package dao;

import model.Loan;
import java.sql.Connection; // Importar Connection
import java.sql.SQLException;
import java.util.List;

public interface ILoanDAO {

    // CORE DATA OPERATIONS (CRUD)

    /**
     * Inserts a new loan record into the database.
     * CRITICAL FIX: Accepts an existing Connection for transaction management.
     * @param loan The Loan object to save.
     * @param conn The JDBC connection controlled by the Service layer.
     * @return The inserted Loan object with the generated ID.
     * @throws SQLException Database access error.
     */
    Loan insert(Loan loan, Connection conn) throws SQLException; // <-- CORREGIDO

    /**
     * Finds a loan record by its unique identifier.
     * @param id The ID of the loan to search for.
     * @return The found Loan object or null.
     * @throws SQLException Database access error.
     */
    Loan findById(int id) throws SQLException;

    /**
     * Updates an existing loan to mark it as returned and set fine/date.
     * CRITICAL FIX: Accepts an existing Connection for transaction management.
     * @param loan The Loan object with updated return details.
     * @param conn The JDBC connection controlled by the Service layer.
     * @return true if the update was successful.
     * @throws SQLException Database access error.
     */
    boolean updateForReturn(Loan loan, Connection conn) throws SQLException; // <-- CORREGIDO


    // LISTING AND REPORTING OPERATIONS (These remain standalone, read-only)

    /**
     * Retrieves all currently active loans (is_returned = FALSE).
     * @return A list of active Loan objects.
     * @throws SQLException Database access error.
     */
    List<Loan> findActiveLoans() throws SQLException;

    /**
     * Retrieves all loans that are past their due date and not returned.
     * Used for the overdue loans report.
     * @return A list of overdue Loan objects.
     * @throws SQLException Database access error.
     */
    List<Loan> findOverdueLoans() throws SQLException;

    /**
     * Retrieves all loan records from the database.
     * @return A list of all Loan objects.
     * @throws SQLException Database access error.
     */
    List<Loan> findAll() throws SQLException;
}