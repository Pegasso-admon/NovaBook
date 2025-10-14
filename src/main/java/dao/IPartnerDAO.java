package dao;

import model.Partner;
import java.sql.SQLException;
import java.util.List;

public interface IPartnerDAO {

    Partner insert(Partner partner) throws SQLException;
    Partner findById(int id) throws SQLException;
    List<Partner> findAll() throws SQLException;
    boolean update(Partner partner) throws SQLException;

    // Validation requirement (Check if partner is active)
    boolean isActive(int id) throws SQLException;
}