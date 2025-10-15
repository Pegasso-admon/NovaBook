package controller;

import service.IUserService;
import model.User;
import exception.BusinessException;
import java.sql.SQLException;
import java.util.List;

/**
 * Controller for User authentication and management.
 */
public class UserController {

    private final IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    /**
     * Authenticates a user.
     * @param username The user's username.
     * @param password The user's password (plaintext).
     * @return The authenticated User object (password field cleared).
     * @throws BusinessException If login fails.
     * @throws SQLException Database access error.
     */
    public User login(String username, String password) throws BusinessException, SQLException {
        return userService.login(username, password);
    }

    /**
     * Registers a new user.
     * @param user The User object to register.
     * @return The registered User object.
     * @throws BusinessException If username is already in use.
     * @throws SQLException Database access error.
     */
    public User registerUser(User user) throws BusinessException, SQLException {
        return userService.register(user);
    }

    /**
     * Retrieves all users.
     * @return List of all User objects.
     * @throws SQLException Database access error.
     */
    public List<User> getAllUsers() throws SQLException {
        return userService.findAll();
    }

    // NOTE: Update and FindById methods are typically included as well.
}