package service.impl;

import dao.IUserDAO;
import exception.BusinessException;
import model.User;
import service.IUserService;
import java.sql.SQLException;
import java.util.List;

public class UserServiceImpl implements IUserService {

    private final IUserDAO userDAO;

    // Dependency injection (constructor injection)
    public UserServiceImpl(IUserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public User login(String username, String password) throws BusinessException, SQLException {
        // Business Logic: Delegate to DAO with the provided password.
        // NOTE: This assumes the password provided is in the format stored in the DB.

        User user = userDAO.findByUsernameAndPassword(username, password);

        if (user == null) {
            // Business rule: Invalid credentials or inactive user.
            throw new BusinessException("Invalid username or password. Please try again.");
        }

        // Security rule: Always clear the password hash/plaintext before returning.
        user.setPassword(null);
        return user;
    }

    @Override
    public User register(User user) throws BusinessException, SQLException {
        // Business Logic: Check for unique username.
        if (userDAO.findByUsername(user.getUsername()) != null) {
            throw new BusinessException("The username '" + user.getUsername() + "' is already in use.");
        }

        // Persist the user with the password already set in the User object.
        return userDAO.insert(user);
    }

    @Override
    public boolean update(User user) throws BusinessException, SQLException {
        // Direct delegation after basic validation (if any).
        return userDAO.update(user);
    }

    @Override
    public User findById(int id) throws SQLException {
        return userDAO.findById(id);
    }

    @Override
    public List<User> findAll() throws SQLException {
        return userDAO.findAll();
    }
}