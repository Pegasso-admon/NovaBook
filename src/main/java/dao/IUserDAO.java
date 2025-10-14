package dao;

import model.User;
import java.sql.SQLException;
import java.util.List;

public interface IUserDAO {

    User insert(User user) throws SQLException;
    User findById(int id) throws SQLException;
    List<User> findAll() throws SQLException;
    boolean update(User user) throws SQLException;

    // Authentication requirement
    User findByUsernameAndPassword(String username, String hashedPassword) throws SQLException;

    // Validation requirement
    User findByUsername(String username) throws SQLException;
}