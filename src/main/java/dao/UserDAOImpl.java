package dao;

import model.User;
import util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAOImpl implements IUserDAO {

    // SQL Statements
    private static final String INSERT_SQL = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
    private static final String FIND_BY_ID_SQL = "SELECT * FROM users WHERE id = ?";
    private static final String FIND_ALL_SQL = "SELECT * FROM users";
    private static final String UPDATE_SQL = "UPDATE users SET username = ?, password = ?, role = ?, is_active = ? WHERE id = ?";
    private static final String FIND_BY_USERNAME_PASSWORD_SQL = "SELECT * FROM users WHERE username = ? AND password = ? AND is_active = TRUE";
    private static final String FIND_BY_USERNAME_SQL = "SELECT * FROM users WHERE username = ?";

    // Utility method to map a ResultSet row to a User object
    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setRole(rs.getString("role"));
        user.setActive(rs.getBoolean("is_active"));
        return user;
    }

    @Override
    public User insert(User user) throws SQLException {
        // IMPORTANT: Retrieves the auto-generated ID from the database
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getRole());

            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        // CRITICAL: Uses setId() to update the Java object with the DB ID
                        user.setId(rs.getInt(1));
                    }
                }
            }
            return user;
        }
    }

    @Override
    public User findById(int id) throws SQLException {
        // Implementation for retrieving a single User by ID
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(FIND_BY_ID_SQL)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUser(rs);
                }
                return null;
            }
        }
    }

    @Override
    public List<User> findAll() throws SQLException {
        // Implementation for retrieving all Users
        List<User> users = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(FIND_ALL_SQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
        }
        return users;
    }

    @Override
    public boolean update(User user) throws SQLException {
        // Implementation for updating an existing User's details
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_SQL)) {

            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getRole());
            ps.setBoolean(4, user.isActive());
            ps.setInt(5, user.getId()); // Used in the WHERE clause

            return ps.executeUpdate() > 0; // Returns true if one row was affected
        }
    }

    @Override
    public User findByUsernameAndPassword(String username, String hashedPassword) throws SQLException {
        // Used for the login process (Authentication)
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(FIND_BY_USERNAME_PASSWORD_SQL)) {

            ps.setString(1, username);
            ps.setString(2, hashedPassword);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUser(rs);
                }
                return null;
            }
        }
    }

    @Override
    public User findByUsername(String username) throws SQLException {
        // Used for validation (checking for username uniqueness)
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(FIND_BY_USERNAME_SQL)) {

            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUser(rs);
                }
                return null;
            }
        }
    }
}