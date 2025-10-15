package view;

import controller.UserController;
import model.User;
import util.AppLogger;

import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JPanel;
import java.awt.GridLayout;

/**
 * Login view using JOptionPane for authentication.
 */
public class LoginView {

    private final UserController userController;

    public LoginView(UserController userController) {
        this.userController = userController;
    }

    /**
     * Displays the login dialog and authenticates the user.
     * @return Authenticated User object, or null if login is cancelled
     */
    public User showLogin() {
        while (true) {
            // Create custom panel with username and password fields
            JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));
            JTextField usernameField = new JTextField(15);
            JPasswordField passwordField = new JPasswordField(15);

            panel.add(new javax.swing.JLabel("Username:"));
            panel.add(usernameField);
            panel.add(new javax.swing.JLabel("Password:"));
            panel.add(passwordField);

            int option = JOptionPane.showConfirmDialog(
                    null,
                    panel,
                    "NovaBook - Login",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE
            );

            // User cancelled
            if (option != JOptionPane.OK_OPTION) {
                return null;
            }

            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());

            // Validate empty fields
            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(
                        null,
                        "Username and password are required.",
                        "Login Error",
                        JOptionPane.ERROR_MESSAGE
                );
                continue;
            }

            // Attempt login
            try {
                User user = userController.login(username, password);

                JOptionPane.showMessageDialog(
                        null,
                        "Welcome, " + user.getUsername() + "!\nRole: " + user.getRole(),
                        "Login Successful",
                        JOptionPane.INFORMATION_MESSAGE
                );

                return user;

            } catch (Exception e) {
                AppLogger.logError("Login failed for username: " + username, e);
                JOptionPane.showMessageDialog(
                        null,
                        "Login failed: " + e.getMessage() + "\n\nPlease try again.",
                        "Login Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }
}