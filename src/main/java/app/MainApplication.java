package app;

import controller.*;
import dao.*;
import service.*;
import service.impl.*;
import view.LoginView;
import view.PrincipalMenuView;
import model.User;
import util.AppLogger;

/**
 * Main application entry point.
 * Initializes dependencies and starts the application flow.
 */
public class MainApplication {

    // Current authenticated user
    private static User currentUser;

    public static void main(String[] args) {
        try {
            AppLogger.logInfo("=== NovaBook System Starting ===");

            // Initialize DAOs
            IBookDAO bookDAO = new BookDAOImpl();
            IUserDAO userDAO = new UserDAOImpl();
            IPartnerDAO partnerDAO = new PartnerDAOImpl();
            ILoanDAO loanDAO = new LoanDAOImpl();

            // Initialize Services
            IBookService bookService = new BookServiceImpl(bookDAO);
            IUserService userService = new UserServiceImpl(userDAO);
            IPartnerService partnerService = new PartnerServiceImpl(partnerDAO);
            ILoanService loanService = new LoanServiceImpl(loanDAO, bookDAO, partnerDAO);

            // Initialize Controllers
            BookController bookController = new BookController(bookService);
            UserController userController = new UserController(userService);
            PartnerController partnerController = new PartnerController(partnerService);
            LoanController loanController = new LoanController(loanService);

            AppLogger.logInfo("All dependencies initialized successfully.");

            // Start Login Flow
            LoginView loginView = new LoginView(userController);
            currentUser = loginView.showLogin();

            if (currentUser != null) {
                AppLogger.logSuccess("User Login", "User: " + currentUser.getUsername() + " | Role: " + currentUser.getRole());

                // Show Main Menu
                PrincipalMenuView mainMenu = new PrincipalMenuView(
                        currentUser,
                        bookController,
                        userController,
                        partnerController,
                        loanController
                );
                mainMenu.showMainMenu();

                AppLogger.logInfo("User logged out: " + currentUser.getUsername());
            } else {
                AppLogger.logWarning("Login cancelled by user.");
            }

            AppLogger.logInfo("=== NovaBook System Shutdown ===");

        } catch (Exception e) {
            AppLogger.logError("Critical error in MainApplication", e);
            System.exit(1);
        }
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User user) {
        currentUser = user;
    }
}