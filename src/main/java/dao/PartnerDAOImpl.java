package dao;

import model.Partner;
import util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PartnerDAOImpl implements IPartnerDAO {

    private static final String INSERT_SQL = "INSERT INTO partners (name, email) VALUES (?, ?)";
    private static final String FIND_BY_ID_SQL = "SELECT * FROM partners WHERE id = ?";
    private static final String FIND_ALL_SQL = "SELECT * FROM partners";
    private static final String UPDATE_SQL = "UPDATE partners SET name = ?, email = ?, is_active = ? WHERE id = ?";
    private static final String IS_ACTIVE_SQL = "SELECT is_active FROM partners WHERE id = ?";

    private Partner mapResultSetToPartner(ResultSet rs) throws SQLException {
        Partner partner = new Partner();
        partner.setId(rs.getInt("id"));
        partner.setName(rs.getString("name"));
        partner.setEmail(rs.getString("email"));
        partner.setActive(rs.getBoolean("is_active"));
        return partner;
    }

    @Override
    public Partner insert(Partner partner) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             // IMPORTANT: Use RETURN_GENERATED_KEYS to retrieve the auto-generated ID
             PreparedStatement ps = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, partner.getName());
            ps.setString(2, partner.getEmail());

            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        partner.setId(rs.getInt(1)); // CORRECTION: Uses the vital setId() method
                    }
                }
            }
            return partner;
        }
    }

    @Override
    public Partner findById(int id) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(FIND_BY_ID_SQL)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToPartner(rs);
                }
                return null;
            }
        }
    }

    @Override
    public List<Partner> findAll() throws SQLException {
        List<Partner> partners = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(FIND_ALL_SQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                partners.add(mapResultSetToPartner(rs));
            }
        }
        return partners;
    }

    @Override
    public boolean update(Partner partner) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_SQL)) {

            ps.setString(1, partner.getName());
            ps.setString(2, partner.getEmail());
            ps.setBoolean(3, partner.isActive());
            ps.setInt(4, partner.getId()); // WHERE clause

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean isActive(int id) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(IS_ACTIVE_SQL)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getBoolean("is_active");
                }
                return false; // Partner not found or inactive
            }
        }
    }
}