package service;

import exception.BusinessException;
import model.User;
import java.sql.SQLException;
import java.util.List;

// Interface for User business operations (Login, CRUD).
public interface IUserService {

    // Core functionality: Authenticates a user (Login)
    User login(String username, String password) throws BusinessException, SQLException;

    // CRUD operations
    User register(User user) throws BusinessException, SQLException;
    boolean update(User user) throws BusinessException, SQLException;
    User findById(int id) throws SQLException;
    List<User> findAll() throws SQLException;
}