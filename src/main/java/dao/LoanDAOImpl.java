package dao;

import model.Loan;
import util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LoanDAOImpl implements ILoanDAO {

    // SQL Statements
    private static final String INSERT_SQL = "INSERT INTO loans (book_isbn, partner_id, due_date) VALUES (?, ?, ?)";
    private static final String FIND_BY_ID_SQL = "SELECT * FROM loans WHERE id = ?";
    private static final String UPDATE_RETURN_SQL = "UPDATE loans SET return_date = ?, fine = ?, is_returned = TRUE WHERE id = ? AND is_returned = FALSE"; // <-- Added is_returned = FALSE for safety
    private static final String FIND_OVERDUE_SQL = "SELECT * FROM loans WHERE due_date < CURDATE() AND is_returned = FALSE";
    private static final String FIND_ACTIVE_SQL = "SELECT * FROM loans WHERE is_returned = FALSE";
    private static final String FIND_ALL_SQL = "SELECT * FROM loans";

    // Utility method to map a ResultSet row to a Loan object
    private Loan mapResultSetToLoan(ResultSet rs) throws SQLException {
        Loan loan = new Loan();
        loan.setId(rs.getInt("id"));
        loan.setBookIsbn(rs.getString("book_isbn"));
        loan.setPartnerId(rs.getInt("partner_id"));
        loan.setLoanDate(rs.getTimestamp("loan_date"));
        loan.setDueDate(rs.getDate("due_date"));
        loan.setReturnDate(rs.getDate("return_date"));
        loan.setFine(rs.getBigDecimal("fine"));
        loan.setReturned(rs.getBoolean("is_returned"));
        return loan;
    }

    @Override
    // CRITICAL FIX: Accepts Connection and removes internal Connection management
    public Loan insert(Loan loan, Connection conn) throws SQLException {
        // NOTE: The try-with-resources only handles the PreparedStatement, not the Connection
        try (PreparedStatement ps = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, loan.getBookIsbn());
            ps.setInt(2, loan.getPartnerId());
            ps.setDate(3, loan.getDueDate());

            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0) {
                // Should not happen, but indicates no row was inserted
                throw new SQLException("Inserting loan failed, no rows affected.");
            }

            // Retrieve the generated ID (if successful)
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    loan.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Inserting loan failed, no ID obtained.");
                }
            }
            return loan;
        }
        // No catch/finally block for connection management, it's handled in the Service layer
    }

    @Override
    public Loan findById(int id) throws SQLException {
        // Read-only operation, manages its own connection
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(FIND_BY_ID_SQL)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToLoan(rs);
                }
                return null;
            }
        }
    }

    @Override
    // CRITICAL FIX: Accepts Connection and removes internal Connection management
    public boolean updateForReturn(Loan loan, Connection conn) throws SQLException {
        // Marks a loan as returned, recording the return date and fine amount
        // NOTE: The try-with-resources only handles the PreparedStatement, not the Connection
        try (PreparedStatement ps = conn.prepareStatement(UPDATE_RETURN_SQL)) {

            ps.setDate(1, loan.getReturnDate());
            ps.setBigDecimal(2, loan.getFine());
            ps.setInt(3, loan.getId()); // WHERE clause

            return ps.executeUpdate() > 0;
        }
        // No catch/finally block for connection management, it's handled in the Service layer
    }

    @Override
    public List<Loan> findOverdueLoans() throws SQLException {
        // Read-only operation, manages its own connection
        List<Loan> loans = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(FIND_OVERDUE_SQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                loans.add(mapResultSetToLoan(rs));
            }
        }
        return loans;
    }

    @Override
    public List<Loan> findActiveLoans() throws SQLException {
        // Read-only operation, manages its own connection
        List<Loan> loans = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(FIND_ACTIVE_SQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                loans.add(mapResultSetToLoan(rs));
            }
        }
        return loans;
    }

    @Override
    public List<Loan> findAll() throws SQLException {
        // Read-only operation, manages its own connection
        List<Loan> loans = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(FIND_ALL_SQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                loans.add(mapResultSetToLoan(rs));
            }
        }
        return loans;
    }
}