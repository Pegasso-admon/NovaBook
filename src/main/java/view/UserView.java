package view;

import controller.UserController;
import model.User;
import util.AppLogger;

import javax.swing.JOptionPane;
import java.util.List;

/**
 * View for User management operations (ADMIN only).
 */
public class UserView {

    private final UserController userController;
    private final User currentUser;

    public UserView(UserController userController, User currentUser) {
        this.userController = userController;
        this.currentUser = currentUser;
    }

    public void showUserMenu() {
        // Check if user has ADMIN role
        if (!currentUser.getRole().equals("ADMIN")) {
            JOptionPane.showMessageDialog(
                    null,
                    "Access Denied: Only ADMIN users can manage users.",
                    "Permission Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        boolean running = true;

        while (running) {
            String menu = "=== Users Management [ADMIN] ===\n\n" +
                    UIHelper.createMenu(
                            "Register New User",
                            "List All Users"
                    );

            String input = JOptionPane.showInputDialog(null, menu, "Users Menu", JOptionPane.PLAIN_MESSAGE);

            if (input == null || input.trim().equals("0")) {
                running = false;
                continue;
            }

            try {
                int option = Integer.parseInt(input.trim());

                switch (option) {
                    case 1: registerUser(); break;
                    case 2: listAllUsers(); break;
                    default:
                        JOptionPane.showMessageDialog(null, "Invalid option.", "Error", JOptionPane.WARNING_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void registerUser() {
        try {
            String username = JOptionPane.showInputDialog(null, "Enter Username:", "Register User", JOptionPane.PLAIN_MESSAGE);
            if (username == null || username.trim().isEmpty()) return;

            String password = JOptionPane.showInputDialog(null, "Enter Password:", "Register User", JOptionPane.PLAIN_MESSAGE);
            if (password == null || password.trim().isEmpty()) return;

            String[] roles = {"ADMIN", "ASSISTANT"};
            String role = (String) JOptionPane.showInputDialog(
                    null,
                    "Select Role:",
                    "Register User",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    roles,
                    roles[1]
            );

            if (role == null) return;

            User user = new User();
            user.setUsername(username.trim());
            user.setPassword(password.trim()); // In production, hash the password!
            user.setRole(role);
            user.setActive(true);

            User registered = userController.registerUser(user);

            JOptionPane.showMessageDialog(
                    null,
                    "User registered successfully!\n\nUsername: " + registered.getUsername() + "\nRole: " + registered.getRole(),
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (Exception e) {
            AppLogger.logError("Failed to register user", e);
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Registration Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void listAllUsers() {
        try {
            List<User> users = userController.getAllUsers();

            if (users.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No users found.", "Users List", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            String table = UIHelper.formatUsersTable(users);
            JOptionPane.showMessageDialog(null, table, "Users List (" + users.size() + " total)", JOptionPane.PLAIN_MESSAGE);

        } catch (Exception e) {
            AppLogger.logError("Failed to list users", e);
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}